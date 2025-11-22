package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.ILotesInventario;
import com.ues.edu.modelo.LotesInventario;
import com.ues.edu.modelo.estructuras.PrioridadCola;

import java.sql.*;

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
}
