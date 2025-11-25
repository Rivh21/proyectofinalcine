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

    private final Conexion conectar;

    public FacturaConcesionDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<FacturaConcesion> selectAll() {
        String sql = "SELECT f.id_factura_concesion, f.monto_total, " +
                     "f.id_metodo_pago, f.id_empleado, " +
                     "e.nombre AS nombre_empleado, m.nombre_metodo " +
                     "FROM factura_concesion f " +
                     "LEFT JOIN empleados e ON f.id_empleado = e.id_empleado " +
                     "LEFT JOIN metodo_pago m ON f.id_metodo_pago = m.id_metodo_pago " +
                     "ORDER BY f.id_factura_concesion ASC";
        return select(sql);
    }

    @Override
    public FacturaConcesion buscarPorId(int id) {
        FacturaConcesion factura = null;
        String sql = "SELECT f.id_factura_concesion, f.monto_total, f.id_metodo_pago, f.id_empleado " +
                     "FROM factura_concesion f WHERE f.id_factura_concesion = ?";
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                factura = new FacturaConcesion();
                factura.setIdFacturaConcesion(rs.getInt(1));
                factura.setMontoTotal(rs.getDouble(2));

                int idMetodo = rs.getInt(3);
                if (idMetodo > 0) factura.setMetodoPago(buscarMetodoPago(idMetodo));

                int idEmpleado = rs.getInt(4);
                if (idEmpleado > 0) factura.setEmpleado(buscarEmpleado(idEmpleado));

                factura.setDetalleConcesion(selectDetalles(id));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return factura;
    }

    @Override
    public boolean insert(FacturaConcesion factura) {
        String sql = "INSERT INTO factura_concesion(monto_total, id_metodo_pago, id_empleado) VALUES (?, ?, ?)";
        return alterarRegistro(sql, factura, true);
    }

    @Override
    public boolean update(FacturaConcesion factura) {
        String sql = "UPDATE factura_concesion SET monto_total=?, id_metodo_pago=?, id_empleado=? " +
                     "WHERE id_factura_concesion=?";
        return alterarRegistro(sql, factura, false);
    }

    @Override
    public boolean delete(FacturaConcesion factura) {
        eliminarDetalles(factura.getIdFacturaConcesion());
        String sql = "DELETE FROM factura_concesion WHERE id_factura_concesion=?";
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, factura.getIdFacturaConcesion());
            ps.execute();
            return true;

        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la factura", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }

        return false;
    }

   

    private ListaSimple<FacturaConcesion> select(String sql) {
        ListaSimple<FacturaConcesion> lista = new ListaSimple<>();
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FacturaConcesion f = new FacturaConcesion();
                f.setIdFacturaConcesion(rs.getInt("id_factura_concesion"));
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
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, FacturaConcesion factura, boolean insertar) {
        Connection con = null;
        PreparedStatement psFactura = null;
        boolean exito = false;

        try {
            con = conectar.getConexion();
            con.setAutoCommit(false);

            psFactura = con.prepareStatement(sql, insertar ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

            psFactura.setDouble(1, factura.getMontoTotal());
            psFactura.setInt(2, factura.getMetodoPago() != null ? factura.getMetodoPago().getidMetodoPago() : 0);
            psFactura.setInt(3, factura.getEmpleado() != null ? factura.getEmpleado().getIdEmpleado() : 0);

            if (!insertar) psFactura.setInt(4, factura.getIdFacturaConcesion());

            psFactura.executeUpdate();

            int facturaId;
            if (insertar) {
                ResultSet rs = psFactura.getGeneratedKeys();
                if (rs.next()) {
                    facturaId = rs.getInt(1);
                    factura.setIdFacturaConcesion(facturaId);
                    insertarDetalles(factura.getDetalleConcesion(), facturaId);
                    exito = true;
                }
                rs.close();
            } else {
                facturaId = factura.getIdFacturaConcesion();
                eliminarDetalles(facturaId);
                insertarDetalles(factura.getDetalleConcesion(), facturaId);
                exito = true;
            }

            if (exito) con.commit();
            else con.rollback();

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            DesktopNotify.showDesktopMessage("Error", "Error al guardar factura. Transacción revertida.", DesktopNotify.ERROR, 5000);
            e.printStackTrace();
            return false;
        } finally {
            try { if (psFactura != null) psFactura.close(); if (con != null) con.setAutoCommit(true); if (con != null) con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }

        return exito;
    }

    private ListaSimple<DetalleConcesion> selectDetalles(int facturaId) {
        ListaSimple<DetalleConcesion> lista = new ListaSimple<>();
        String sql = "SELECT dc.id_detalle_concesion, dc.id_producto, dc.cantidad, dc.precio_unitario, " +
                     "(dc.cantidad * dc.precio_unitario) AS subtotal, " +
                     "p.nombre AS nombre_producto " +
                     "FROM detalle_concesion dc " +
                     "JOIN producto p ON dc.id_producto = p.id_producto " +
                     "WHERE dc.id_factura_concesion=?";
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleConcesion d = new DetalleConcesion();
                d.setIdDetalle(rs.getInt("id_detalle_concesion"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                d.setSubtotal(rs.getDouble("subtotal"));

                Producto p = new Producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre_producto"));
                d.setProducto(p);

                lista.insertar(d);
            }

        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error en detalles", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }

        return lista;
    }

    private void insertarDetalles(ListaSimple<DetalleConcesion> detalles, int facturaId) {
        if (detalles == null) return;

        String sql = "INSERT INTO detalle_concesion(id_factura_concesion, id_producto, cantidad, precio_unitario) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            var lista = detalles.toArray();
            for (DetalleConcesion d : lista) {
                ps.setInt(1, facturaId);
                ps.setInt(2, d.getProducto().getIdProducto());
                ps.setInt(3, d.getCantidad());
                ps.setDouble(4, d.getPrecioUnitario());
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error al insertar detalles", DesktopNotify.ERROR, 5000);
            e.printStackTrace();
        }
    }

    private void eliminarDetalles(int facturaId) {
        String sql = "DELETE FROM detalle_concesion WHERE id_factura_concesion=?";
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            ps.execute();

        } catch (SQLException e) {
            DesktopNotify.showDesktopMessage("Error", "Error eliminando detalles", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }
    }

    private MetodoPago buscarMetodoPago(int id) {
        MetodoPagoDao dao = new MetodoPagoDao();
        ListaSimple<MetodoPago> lista = dao.selectAll();
        for (MetodoPago mp : lista.toArray()) {
            if (mp.getidMetodoPago() == id) return mp;
        }
        return null;
    }

    private Empleado buscarEmpleado(int id) {
        EmpleadoDao dao = new EmpleadoDao();
        ListaSimple<Empleado> lista = dao.selectAll();
        for (Empleado e : lista.toArray()) {
            if (e.getIdEmpleado() == id) return e;
        }
        return null;
    }
}
