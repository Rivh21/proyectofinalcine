/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.modelo.dao;
import com.ues.edu.interfaces.ILotesInventario;
import com.ues.edu.modelo.LotesInventario;
import com.ues.edu.modelo.estructuras.PrioridadCola;
import java.sql.*;
/**
 *
 * @author radon
 */
public class LotesInventarioDao implements ILotesInventario {

    private PrioridadCola<LotesInventario> cola = new PrioridadCola<>(3);

    @Override
    public PrioridadCola<LotesInventario> selectAll() {
        cola = new PrioridadCola<>(3);

        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                   + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                   + "FROM lotes_inventario l "
                   + "INNER JOIN producto p ON l.id_producto = p.id_producto";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LotesInventario l = new LotesInventario();
                l.setIdLote(rs.getInt("id_lote"));
                l.setIdProducto(rs.getInt("id_producto"));
                l.setProductoNombre(rs.getString("producto_nombre"));
                l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                l.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                l.setFechaRecepcion(rs.getDate("fecha_recepcion").toLocalDate());
                l.calcularPrioridad(); // usar prioridad de la clase
                cola.offer(l, l.getPrioridad());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cola;
    }

    @Override
    public boolean insert(LotesInventario lote) {
        String sql = "INSERT INTO lotes_inventario "
                   + "(id_producto, cantidad_disponible, fecha_caducidad, fecha_recepcion) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, lote.getIdProducto());
            ps.setInt(2, lote.getCantidadDisponible());
            ps.setDate(3, java.sql.Date.valueOf(lote.getFechaCaducidad()));
            ps.setDate(4, java.sql.Date.valueOf(lote.getFechaRecepcion()));

            int filas = ps.executeUpdate();
            if (filas == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    lote.setIdLote(keys.getInt(1));
                }
            }

            lote.calcularPrioridad();
            cola.offer(lote, lote.getPrioridad());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(LotesInventario lote) {
        String sql = "UPDATE lotes_inventario SET "
                   + "id_producto=?, cantidad_disponible=?, fecha_caducidad=?, fecha_recepcion=? "
                   + "WHERE id_lote=?";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lote.getIdProducto());
            ps.setInt(2, lote.getCantidadDisponible());
            ps.setDate(3, java.sql.Date.valueOf(lote.getFechaCaducidad()));
            ps.setDate(4, java.sql.Date.valueOf(lote.getFechaRecepcion()));
            ps.setInt(5, lote.getIdLote());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) selectAll();
            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(LotesInventario lote) {
        String sql = "DELETE FROM lotes_inventario WHERE id_lote=?";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lote.getIdLote());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) selectAll();
            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LotesInventario buscar(int idLote) {
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                   + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                   + "FROM lotes_inventario l "
                   + "INNER JOIN producto p ON l.id_producto = p.id_producto "
                   + "WHERE l.id_lote=?";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLote);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LotesInventario l = new LotesInventario();
                    l.setIdLote(rs.getInt("id_lote"));
                    l.setIdProducto(rs.getInt("id_producto"));
                    l.setProductoNombre(rs.getString("producto_nombre"));
                    l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    l.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                    l.setFechaRecepcion(rs.getDate("fecha_recepcion").toLocalDate());
                    l.calcularPrioridad();
                    return l;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LotesInventario buscarPorNombreProducto(String nombre) {
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                   + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                   + "FROM lotes_inventario l "
                   + "INNER JOIN producto p ON l.id_producto = p.id_producto "
                   + "WHERE p.nombre LIKE ? LIMIT 1";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LotesInventario l = new LotesInventario();
                    l.setIdLote(rs.getInt("id_lote"));
                    l.setIdProducto(rs.getInt("id_producto"));
                    l.setProductoNombre(rs.getString("producto_nombre"));
                    l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    l.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                    l.setFechaRecepcion(rs.getDate("fecha_recepcion").toLocalDate());
                    l.calcularPrioridad();
                    return l;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int obtenerCantidadDisponible(int idProducto) { 
        String sql = "SELECT COALESCE(SUM(cantidad_disponible), 0) AS total_disponible " +
                     "FROM lotes_inventario " +
                     "WHERE id_producto = ?";

        try (Connection conn = new Conexion().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_disponible");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean consumir(int idProducto, int cantidadAconsumir) {
        
   
        String sqlSelectLotes = "SELECT id_lote, cantidad_disponible FROM lotes_inventario "
                              + "WHERE id_producto = ? AND cantidad_disponible > 0 "
                              + "ORDER BY fecha_caducidad ASC"; 

        String sqlUpdateLote = "UPDATE lotes_inventario SET cantidad_disponible = ? WHERE id_lote = ?";
        String sqlDeleteLote = "DELETE FROM lotes_inventario WHERE id_lote = ?";

        int cantidadRestante = cantidadAconsumir;
        Connection conn = null;

        try {
            conn = new Conexion().getConexion();
            conn.setAutoCommit(false); 

            
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectLotes)) {
                psSelect.setInt(1, idProducto);

                try (ResultSet rs = psSelect.executeQuery()) {
                    while (rs.next() && cantidadRestante > 0) {
                        int idLote = rs.getInt("id_lote");
                        int disponible = rs.getInt("cantidad_disponible");

                        if (disponible <= cantidadRestante) {
                         
                            cantidadRestante -= disponible;
                            
                            try (PreparedStatement psDelete = conn.prepareStatement(sqlDeleteLote)) {
                                psDelete.setInt(1, idLote);
                                psDelete.executeUpdate();
                            }
                        } else {
                            
                            int nuevaCantidad = disponible - cantidadRestante;
                            
                            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateLote)) {
                                psUpdate.setInt(1, nuevaCantidad);
                                psUpdate.setInt(2, idLote);
                                psUpdate.executeUpdate();
                            }
                            cantidadRestante = 0; 
                        }
                    }
                }
            }

            if (cantidadRestante == 0) {
                conn.commit(); 
                return true;
            } else {
                conn.rollback(); 
                System.err.println("Advertencia: No se pudo consumir el stock completo para ID " + idProducto + ". Cantidad restante: " + cantidadRestante);
                return false; 
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  

    
}
