/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.modelo.dao;
import com.ues.edu.interfaces.IDetalleConcesion;
import com.ues.edu.modelo.DetalleConcesion;
import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.estructuras.Pila;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleConcesionDao implements IDetalleConcesion {

    private Conexion conectar;

    public DetalleConcesionDao() {
        conectar = new Conexion();
    }
    
    @Override
    public Pila<DetalleConcesion> selectAll() {
        String sql = "SELECT * FROM detalle_concesion";
        return select(sql);
    }

    @Override
    public Pila<DetalleConcesion> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT * FROM detalle_concesion WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public Pila<DetalleConcesion> selectByFactura(int idFactura) {
        String sql = "SELECT * FROM detalle_concesion WHERE id_factura_concesion=" + idFactura;
        return select(sql);
    }
    


    @Override
    public boolean insert(DetalleConcesion obj) {
        String sql = "INSERT INTO detalle_concesion(cantidad, precio_unitario, subtotal, id_factura_concesion, id_producto) "
                   + "VALUES (?, ?, ?, ?, ?)";        
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, obj.getCantidad());
            ps.setDouble(2, obj.getPrecioUnitario());
            ps.setDouble(3, obj.getSubtotal()); 
            ps.setInt(4, obj.getFactura() != null ? obj.getFactura().getIdFacturaConcesion() : 0);
            ps.setInt(5, obj.getProducto() != null ? obj.getProducto().getIdProducto() : 0);

            ps.execute();
            return true;

        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al insertar detalle", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }

        return false;
    }
   
    @Override
    public boolean delete(DetalleConcesion obj) {
        String sql = "DELETE FROM detalle_concesion WHERE id_detalle_concesion=?";
        try (Connection con = conectar.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdDetalle());
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al eliminar detalle", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }
        return false;
    }
    private Pila<DetalleConcesion> select(String sql) {
        Pila<DetalleConcesion> pila = new Pila<>();
        try (Connection con = conectar.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DetalleConcesion d = new DetalleConcesion();
                d.setIdDetalle(rs.getInt("id_detalle_concesion")); 
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                d.setSubtotal(rs.getDouble("subtotal")); // ⭐ Asegurado: la columna 'subtotal' se usa

                FacturaConcesion f = new FacturaConcesion();
                f.setIdFacturaConcesion(rs.getInt("id_factura_concesion"));
                d.setFactura(f);

                d.setProducto(new Producto(rs.getInt("id_producto")));

                pila.push(d);
            }

        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al consultar detalles", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        }

        return pila;
    }
}