/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IFacturaConcesion;
import com.ues.edu.modelo.*;
import com.ues.edu.modelo.estructuras.ListaSimple;
import ds.desktop.notify.DesktopNotify;
import java.sql.*;

/**
 *
 * @author radon
 */
public class FacturaConcesionDao implements IFacturaConcesion {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public FacturaConcesionDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<FacturaConcesion> selectAll() {
        String sql = "SELECT f.id_factura_concesion, f.monto_total, "
                + "f.id_metodo_pago, f.id_empleado, "
                + "e.nombre AS nombre_empleado, m.nombre_metodo "
                + "FROM factura_concesion f "
                + "LEFT JOIN empleados e ON f.id_empleado = e.id_empleado "
                + "LEFT JOIN metodo_pago m ON f.id_metodo_pago = m.id_metodo_pago "
                + "ORDER BY f.id_factura_concesion ASC";
        return select(sql);
    }

    @Override
    public FacturaConcesion buscarPorId(String id) {
        String sql = "SELECT f.id_factura_concesion, f.monto_total, f.id_metodo_pago, f.id_empleado "
                + "FROM factura_concesion f WHERE f.id_factura_concesion = ?";
        return ejecutarBuscarPorId(sql, id);
    }

    @Override
    public ListaSimple<FacturaConcesion> buscar(String texto) {
        String sql = "SELECT f.id_factura_concesion, f.monto_total, "
                + "f.id_metodo_pago, f.id_empleado, "
                + "e.nombre AS nombre_empleado, m.nombre_metodo "
                + "FROM factura_concesion f "
                + "LEFT JOIN empleados e ON f.id_empleado = e.id_empleado "
                + "LEFT JOIN metodo_pago m ON f.id_metodo_pago = m.id_metodo_pago "
                + "WHERE f.id_factura_concesion LIKE '%" + texto + "%' "
                + "OR e.nombre LIKE '%" + texto + "%' "
                + "OR m.nombre_metodo LIKE '%" + texto + "%' "
                + "ORDER BY f.id_factura_concesion ASC";
        return select(sql);
    }

    @Override
    public boolean insert(FacturaConcesion factura) {
        String sql = "INSERT INTO factura_concesion(id_factura_concesion, monto_total, id_metodo_pago, id_empleado) VALUES (?, ?, ?, ?)";
        return alterarRegistro(sql, factura, true);
    }

    @Override
    public boolean update(FacturaConcesion factura) {
        String sql = "UPDATE factura_concesion SET monto_total=?, id_metodo_pago=?, id_empleado=? "
                + "WHERE id_factura_concesion=?";
        return alterarRegistro(sql, factura, false);
    }

    @Override
    public boolean delete(FacturaConcesion factura) {
        eliminarDetalles(factura.getIdFacturaConcesion());
        String sql = "DELETE FROM factura_concesion WHERE id_factura_concesion=?";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, factura.getIdFacturaConcesion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la factura", DesktopNotify.ERROR, 3000);
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

    @Override
    public boolean anularFacturaConcesion(String idFactura) {
        String sql = "DELETE FROM factura_concesion WHERE id_factura_concesion = ?";
        return ejecutarAnular(sql, idFactura);
    }

    private ListaSimple<FacturaConcesion> select(String sql) {
        ListaSimple<FacturaConcesion> lista = new ListaSimple<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                FacturaConcesion f = new FacturaConcesion();

                f.setIdFacturaConcesion(rs.getString("id_factura_concesion"));
                f.setMontoTotal(rs.getDouble("monto_total"));

                int idEmp = rs.getInt("id_empleado");
                if (idEmp > 0) {
                    Empleado emp = new Empleado(idEmp);
                    emp.setNombre(rs.getString("nombre_empleado"));
                    f.setEmpleado(emp);
                }

                int idMet = rs.getInt("id_metodo_pago");
                if (idMet > 0) {
                    MetodoPago mp = new MetodoPago(idMet);
                    mp.setnombreMetodo(rs.getString("nombre_metodo"));
                    f.setMetodoPago(mp);
                }

                f.setDetalleConcesion(selectDetalles(f.getIdFacturaConcesion()));
                lista.insertar(f);
            }
        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error en SELECT", DesktopNotify.ERROR, 3000);
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
        return lista;
    }

    private FacturaConcesion ejecutarBuscarPorId(String sql, String id) {
        FacturaConcesion factura = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                factura = new FacturaConcesion();
                factura.setIdFacturaConcesion(rs.getString(1));
                factura.setMontoTotal(rs.getDouble(2));

                int idMetodo = rs.getInt(3);
                if (idMetodo > 0) {
                    factura.setMetodoPago(buscarMetodoPago(idMetodo));
                }

                int idEmpleado = rs.getInt(4);
                if (idEmpleado > 0) {
                    factura.setEmpleado(buscarEmpleado(idEmpleado));
                }
                factura.setDetalleConcesion(selectDetalles(id));
            }
        } catch (SQLException e) {
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
        return factura;
    }

    private boolean alterarRegistro(String sql, FacturaConcesion factura, boolean insertar) {
        Connection con = null;
        PreparedStatement psFactura = null;
        boolean exito = false;

        try {
            con = conectar.getConexion();
            con.setAutoCommit(false);

            psFactura = con.prepareStatement(sql, Statement.NO_GENERATED_KEYS);

            if (insertar) {
                psFactura.setString(1, factura.getIdFacturaConcesion());
                psFactura.setDouble(2, factura.getMontoTotal());
                psFactura.setInt(3, factura.getMetodoPago() != null ? factura.getMetodoPago().getidMetodoPago() : 0);
                psFactura.setInt(4, factura.getEmpleado() != null ? factura.getEmpleado().getIdEmpleado() : 0);
            } else {
                psFactura.setDouble(1, factura.getMontoTotal());
                psFactura.setInt(2, factura.getMetodoPago() != null ? factura.getMetodoPago().getidMetodoPago() : 0);
                psFactura.setInt(3, factura.getEmpleado() != null ? factura.getEmpleado().getIdEmpleado() : 0);
                psFactura.setString(4, factura.getIdFacturaConcesion());
            }

            psFactura.executeUpdate();
            String facturaId = factura.getIdFacturaConcesion();

            if (insertar) {
                insertarDetalles(factura.getDetalleConcesion(), facturaId, con);
                LotesInventarioDao lotesDao = new LotesInventarioDao();
                var detalles = factura.getDetalleConcesion().toArray();
                for (int i = 0; i < detalles.size(); i++) {
                    DetalleConcesion d = detalles.get(i);
                    int idProducto = d.getProducto().getIdProducto();
                    int cantidad = d.getCantidad();
                    if (!lotesDao.consumir(idProducto, cantidad)) {
                        throw new SQLException("Error crítico: Falló al consumir el stock para producto ID: " + idProducto);
                    }
                }
                exito = true;
            } else {
                eliminarDetalles(facturaId, con);
                insertarDetalles(factura.getDetalleConcesion(), facturaId, con);
                exito = true;
            }

            if (exito) {
                con.commit();
            } else {
                con.rollback();
            }

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            DesktopNotify.showDesktopMessage("Error", "Error al procesar factura. Transacción revertida.", DesktopNotify.ERROR, 5000);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psFactura != null) {
                    psFactura.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return exito;
    }

    private ListaSimple<DetalleConcesion> selectDetalles(String facturaId) {
        ListaSimple<DetalleConcesion> lista = new ListaSimple<>();
        String sql = "SELECT dc.id_detalle_concesion, dc.id_producto, dc.cantidad, dc.precio_unitario, "
                + "(dc.cantidad * dc.precio_unitario) AS subtotal, "
                + "p.nombre AS nombre_producto "
                + "FROM detalle_concesion dc "
                + "JOIN producto p ON dc.id_producto = p.id_producto "
                + "WHERE dc.id_factura_concesion=?";

        Connection localCon = null;
        PreparedStatement localPs = null;
        ResultSet localRs = null;

        try {
            localCon = conectar.getConexion();
            localPs = localCon.prepareStatement(sql);
            localPs.setString(1, facturaId);
            localRs = localPs.executeQuery();

            while (localRs.next()) {
                DetalleConcesion d = new DetalleConcesion();
                d.setIdDetalle(localRs.getInt("id_detalle_concesion"));
                d.setCantidad(localRs.getInt("cantidad"));
                d.setPrecioUnitario(localRs.getDouble("precio_unitario"));
                d.setSubtotal(localRs.getDouble("subtotal"));

                Producto p = new Producto(localRs.getInt("id_producto"));
                p.setNombre(localRs.getString("nombre_producto"));
                d.setProducto(p);

                lista.insertar(d);
            }
        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error en detalles", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (localRs != null) {
                    localRs.close();
                }
                if (localPs != null) {
                    localPs.close();
                }
                conectar.closeConexion(localCon);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    private void insertarDetalles(ListaSimple<DetalleConcesion> detalles, String facturaId, Connection con) throws SQLException {
        if (detalles == null) {
            return;
        }
        String sql = "INSERT INTO detalle_concesion(id_factura_concesion, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            var lista = detalles.toArray();
            for (int i = 0; i < lista.size(); i++) {
                DetalleConcesion d = lista.get(i);
                ps.setString(1, facturaId);
                ps.setInt(2, d.getProducto().getIdProducto());
                ps.setInt(3, d.getCantidad());
                ps.setDouble(4, d.getPrecioUnitario());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void eliminarDetalles(String facturaId, Connection con) throws SQLException {
        String sql = "DELETE FROM detalle_concesion WHERE id_factura_concesion=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, facturaId);
            ps.execute();
        }
    }

    private void eliminarDetalles(String facturaId) {
        String sql = "DELETE FROM detalle_concesion WHERE id_factura_concesion=?";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, facturaId);
            ps.execute();
        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error eliminando detalles", DesktopNotify.ERROR, 3000);
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
    }

    private MetodoPago buscarMetodoPago(int id) {
        MetodoPagoDao dao = new MetodoPagoDao();
        ListaSimple<MetodoPago> lista = dao.selectAll();
        for (MetodoPago mp : lista.toArray()) {
            if (mp.getidMetodoPago() == id) {
                return mp;
            }
        }
        return null;
    }

    private Empleado buscarEmpleado(int id) {
        EmpleadoDao dao = new EmpleadoDao();
        ListaSimple<Empleado> lista = dao.selectAll();
        for (Empleado e : lista.toArray()) {
            if (e.getIdEmpleado() == id) {
                return e;
            }
        }
        return null;
    }

    private boolean ejecutarAnular(String sql, String idFactura) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, idFactura);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "No se pudo anular la factura", DesktopNotify.ERROR, 4000);
            e.printStackTrace();
            return false;
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
    }
}
