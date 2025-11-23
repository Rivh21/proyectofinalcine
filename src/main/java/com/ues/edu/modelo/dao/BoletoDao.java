/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IBoleto;
import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.Boleto;
import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.estructuras.ListaSimple;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author jorge
 */
public class BoletoDao implements IBoleto {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public BoletoDao() {
        conectar = new Conexion();
    }

    @Override
    public boolean existeIdFactura(String id) {
        String sql = "SELECT 1 FROM facturas_taquillas WHERE id_factura_taquilla = ? LIMIT 1";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public HashSet<Integer> getAsientosOcupados(int idFuncion) {
        String sql = "SELECT id_asiento FROM boletos WHERE id_funcion = ?";
        HashSet<Integer> asientosOcupados = new HashSet<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idFuncion);
            rs = ps.executeQuery();

            while (rs.next()) {
                asientosOcupados.add(rs.getInt("id_asiento"));
            }
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Ocupación", "Error al verificar asientos ocupados.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return asientosOcupados;
    }

    @Override
    public ListaSimple<Boleto> getBoletosPorFactura(String idFactura) {
        String sql = "SELECT p.titulo, s.nombre_sala, f.fecha_hora_inicio, b.precio_pagado, b.fecha_venta, a.fila, a.numero "
                + "FROM boletos b "
                + "JOIN funciones f ON b.id_funcion = f.id_funcion "
                + "JOIN peliculas p ON f.id_pelicula = p.id_pelicula "
                + "JOIN salas s ON f.id_sala = s.id_sala "
                + "JOIN asientos a ON b.id_asiento = a.id_asiento "
                + "WHERE b.id_factura_taquilla = ?";

        ListaSimple<Boleto> lista = new ListaSimple<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, idFactura);
            rs = ps.executeQuery();

            while (rs.next()) {
                Boleto b = new Boleto();
                b.setPrecioPagado(rs.getBigDecimal("precio_pagado"));
                if (rs.getTimestamp("fecha_venta") != null) {
                    b.setFechaVenta(rs.getTimestamp("fecha_venta").toLocalDateTime());
                }

                Funcion f = new Funcion();
                f.setPeliculaTitulo(rs.getString("titulo"));
                f.setSalaNombre(rs.getString("nombre_sala"));
                f.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                b.setFuncion(f);

                Asiento a = new Asiento();
                a.setFila(rs.getString("fila"));
                a.setNumero(rs.getInt("numero"));
                b.setAsiento(a);

                lista.insertar(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (Exception e) {
            }
        }
        return lista;
    }

    @Override
    public ListaSimple<Boleto> selectAll() {
        String sql = "SELECT b.id_boleto, b.fecha_venta, b.precio_pagado, ft.id_factura_taquilla, f.id_funcion, a.id_asiento "
                + "FROM boletos b JOIN facturas_taquillas ft ON b.id_factura_taquilla = ft.id_factura_taquilla "
                + "JOIN funciones f ON b.id_funcion = f.id_funcion "
                + "JOIN asientos a ON b.id_asiento = a.id_asiento";
        return select(sql);
    }

    @Override
    public ListaSimple<Boleto> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT b.id_boleto, b.fecha_venta, b.precio_pagado, ft.id_factura_taquilla, f.id_funcion, a.id_asiento "
                + "FROM boletos b JOIN facturas_taquillas ft ON b.id_factura_taquilla = ft.id_factura_taquilla "
                + "JOIN funciones f ON b.id_funcion = f.id_funcion "
                + "JOIN asientos a ON b.id_asiento = a.id_asiento "
                + "WHERE b." + atributo + " LIKE '" + condicion + "%'";
        return select(sql);
    }

    @Override
    public boolean generarTransaccion(FacturaTaquilla factura, List<Boleto> boletosVenta) {
        String sqlFactura = "INSERT INTO facturas_taquillas(id_factura_taquilla, id_empleado, id_metodo_pago, monto_total, descuento_aplicado) VALUES (?, ?, ?, ?, ?)";
        String sqlBoleto = "INSERT INTO boletos(id_asiento, id_factura_taquilla, id_funcion, fecha_venta, precio_pagado) VALUES (?, ?, ?, NOW(), ?)";

        Connection localCon = null;
        PreparedStatement psFactura = null;
        PreparedStatement psBoleto = null;

        try {
            localCon = conectar.getConexion();
            localCon.setAutoCommit(false);
            psFactura = localCon.prepareStatement(sqlFactura);
            psFactura.setString(1, factura.getIdFacturaTaquilla());

            psFactura.setInt(2, factura.getEmpleado().getIdEmpleado());
            psFactura.setInt(3, factura.getMetodoPago().getidMetodoPago());
            psFactura.setBigDecimal(4, factura.getMontoTotal());
            psFactura.setBigDecimal(5, factura.getDescuentoAplicado()); // Nueva columna

            psFactura.executeUpdate();
            String idFacturaStr = factura.getIdFacturaTaquilla();

            psBoleto = localCon.prepareStatement(sqlBoleto);

            for (Boleto boleto : boletosVenta) {
                psBoleto.setInt(1, boleto.getAsiento().getIdAsiento());
                psBoleto.setString(2, idFacturaStr);
                psBoleto.setInt(3, boleto.getFuncion().getIdFuncion());
                psBoleto.setBigDecimal(4, boleto.getPrecioPagado());

                psBoleto.addBatch();
            }

            psBoleto.executeBatch();
            localCon.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (localCon != null) {
                    localCon.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Transacción", "Fallo al guardar la venta en BD.", DesktopNotify.ERROR, 6000);
            e.printStackTrace();
            return false;

        } finally {
            // CERRAR RECURSOS
            try {
                if (psFactura != null) {
                    psFactura.close();
                }
                if (psBoleto != null) {
                    psBoleto.close();
                }
                conectar.closeConexion(localCon);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Boleto obj) {
        String sql = "UPDATE boletos SET precio_pagado=? WHERE id_boleto=" + obj.getIdBoleto();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Boleto obj) {
        String sql = "DELETE FROM boletos WHERE id_boleto='" + obj.getIdBoleto() + "'";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error al Eliminar", "Fallo al eliminar boleto.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private ListaSimple<Boleto> select(String sql) {
        ListaSimple<Boleto> lista = new ListaSimple();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Boleto obj = new Boleto();

                Timestamp timestamp = rs.getTimestamp("fecha_venta");
                LocalDateTime fechaVenta = timestamp.toLocalDateTime();
                BigDecimal precioPagado = rs.getBigDecimal("precio_pagado");

                obj.setIdBoleto(rs.getInt("id_boleto"));
                obj.setPrecioPagado(precioPagado);
                obj.setFechaVenta(fechaVenta);

                lista.insertar(obj);
            }
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Consulta", "Error al cargar la lista de boletos.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, Boleto obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);

            ps.setBigDecimal(1, obj.getPrecioPagado());

            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Edición", "Error al editar el boleto.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
