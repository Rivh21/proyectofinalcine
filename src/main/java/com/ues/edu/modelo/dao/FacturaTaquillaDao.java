/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IFacturaTaquilla;
import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.MetodoPago;
import com.ues.edu.modelo.estructuras.ListaSimple;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jorge
 */
public class FacturaTaquillaDao implements IFacturaTaquilla {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public FacturaTaquillaDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<FacturaTaquilla> selectAll() {
        String sql = "SELECT ft.id_factura_taquilla, ft.monto_total, ft.descuento_aplicado, ft.estado, "
                + "e.id_empleado, e.nombre, e.apellido, "
                + "mp.id_metodo_pago, mp.nombre_metodo "
                + "FROM facturas_taquillas ft "
                + "JOIN empleados e ON ft.id_empleado = e.id_empleado "
                + "JOIN metodo_pago mp ON ft.id_metodo_pago = mp.id_metodo_pago";
        return select(sql);
    }

    @Override
    public ListaSimple<FacturaTaquilla> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT ft.id_factura_taquilla, ft.monto_total, ft.descuento_aplicado, ft.estado, "
                + "e.id_empleado, e.nombre, e.apellido, "
                + "mp.id_metodo_pago, mp.nombre_metodo "
                + "FROM facturas_taquillas ft "
                + "JOIN empleados e ON ft.id_empleado = e.id_empleado "
                + "JOIN metodo_pago mp ON ft.id_metodo_pago = mp.id_metodo_pago "
                + "WHERE ft." + atributo + " LIKE '" + condicion + "%'";
        return select(sql);
    }

    @Override
    public ListaSimple<FacturaTaquilla> buscar(String dato) {
        String sql = "SELECT ft.id_factura_taquilla, ft.monto_total, ft.descuento_aplicado, ft.estado, "
                + "e.id_empleado, e.nombre, e.apellido, "
                + "mp.id_metodo_pago, mp.nombre_metodo "
                + "FROM facturas_taquillas ft "
                + "JOIN empleados e ON ft.id_empleado = e.id_empleado "
                + "JOIN metodo_pago mp ON ft.id_metodo_pago = mp.id_metodo_pago "
                + "WHERE ft.id_factura_taquilla LIKE '" + dato + "%' OR mp.nombre_metodo LIKE '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean anularFactura(String idFactura) {
        String sqlAnular = "UPDATE facturas_taquillas SET estado = 'ANULADA', monto_total = 0.00 WHERE id_factura_taquilla = ?";
        String sqlLiberar = "DELETE FROM boletos WHERE id_factura_taquilla = ?";
        return ejecutarAnularFactura(sqlAnular, sqlLiberar, idFactura);
    }

    @Override
    public boolean update(FacturaTaquilla obj) {
        String sql = "UPDATE facturas_taquillas SET id_metodo_pago=?, descuento_aplicado=? WHERE id_factura_taquilla='" + obj.getIdFacturaTaquilla() + "'";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(FacturaTaquilla obj) {
        String sql = "DELETE FROM facturas_taquillas WHERE id_factura_taquilla='" + obj.getIdFacturaTaquilla() + "'";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error al Eliminar", "No se puede eliminar físicamente. Use la opción Anular.", DesktopNotify.ERROR, 3000);
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

    private ListaSimple<FacturaTaquilla> select(String sql) {
        ListaSimple<FacturaTaquilla> lista = new ListaSimple();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                FacturaTaquilla obj = new FacturaTaquilla();
                obj.setIdFacturaTaquilla(rs.getString("id_factura_taquilla"));
                obj.setMontoTotal(rs.getBigDecimal("monto_total"));
                obj.setDescuentoAplicado(rs.getBigDecimal("descuento_aplicado"));
                obj.setEstado(rs.getString("estado"));

                Empleado emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setNombre(rs.getString("nombre"));
                emp.setApellido(rs.getString("apellido"));
                obj.setEmpleado(emp);

                MetodoPago mp = new MetodoPago();
                mp.setidMetodoPago(rs.getInt("id_metodo_pago"));
                mp.setnombreMetodo(rs.getString("nombre_metodo"));
                obj.setMetodoPago(mp);

                lista.insertar(obj);
            }
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Consulta", "Error al cargar la lista de facturas.", DesktopNotify.ERROR, 3000);
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

    private boolean alterarRegistro(String sql, FacturaTaquilla obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, obj.getMetodoPago().getidMetodoPago());
            ps.setBigDecimal(2, obj.getDescuentoAplicado());

            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Edición", "Error al editar la factura.", DesktopNotify.ERROR, 3000);
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

    private boolean ejecutarAnularFactura(String sqlAnular, String sqlLiberar, String idFactura) {
        Connection localCon = null;
        PreparedStatement psAnular = null;
        PreparedStatement psLiberar = null;

        try {
            localCon = conectar.getConexion();
            localCon.setAutoCommit(false);

            psAnular = localCon.prepareStatement(sqlAnular);
            psAnular.setString(1, idFactura);
            psAnular.executeUpdate();

            psLiberar = localCon.prepareStatement(sqlLiberar);
            psLiberar.setString(1, idFactura);
            psLiberar.executeUpdate();

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
            DesktopNotify.showDesktopMessage("Error", "No se pudo anular la factura.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psAnular != null) {
                    psAnular.close();
                }
                if (psLiberar != null) {
                    psLiberar.close();
                }
                conectar.closeConexion(localCon);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
