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
    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public LotesInventarioDao() {
        conectar = new Conexion();
    }

    @Override
    public PrioridadCola<LotesInventario> selectAll() {
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                + "FROM lotes_inventario l "
                + "INNER JOIN producto p ON l.id_producto = p.id_producto";
        return ejecutarSelectAll(sql);
    }

    @Override
    public boolean insert(LotesInventario lote) {
        String sql = "INSERT INTO lotes_inventario "
                + "(id_producto, cantidad_disponible, fecha_caducidad, fecha_recepcion) "
                + "VALUES (?, ?, ?, ?)";
        return ejecutarInsert(sql, lote);
    }

    @Override
    public boolean update(LotesInventario lote) {
        String sql = "UPDATE lotes_inventario SET "
                + "id_producto=?, cantidad_disponible=?, fecha_caducidad=?, fecha_recepcion=? "
                + "WHERE id_lote=?";
        return ejecutarUpdate(sql, lote);
    }

    @Override
    public boolean delete(LotesInventario lote) {
        String sql = "DELETE FROM lotes_inventario WHERE id_lote=?";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, lote.getIdLote());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                selectAll();
            }
            return ok;
        } catch (Exception e) {
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

    @Override
    public LotesInventario buscar(int idLote) {
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                + "FROM lotes_inventario l "
                + "INNER JOIN producto p ON l.id_producto = p.id_producto "
                + "WHERE l.id_lote=?";
        return ejecutarBuscar(sql, idLote);
    }

    @Override
    public LotesInventario buscarPorNombreProducto(String nombre) {
        String sql = "SELECT l.id_lote, l.id_producto, p.nombre AS producto_nombre, "
                + "l.cantidad_disponible, l.fecha_caducidad, l.fecha_recepcion "
                + "FROM lotes_inventario l "
                + "INNER JOIN producto p ON l.id_producto = p.id_producto "
                + "WHERE p.nombre LIKE ? LIMIT 1";
        return ejecutarBuscarPorNombre(sql, nombre);
    }

    public int obtenerCantidadDisponible(int idProducto) {
        String sql = "SELECT COALESCE(SUM(cantidad_disponible), 0) AS total_disponible "
                + "FROM lotes_inventario "
                + "WHERE id_producto = ?";
        return ejecutarObtenerCantidad(sql, idProducto);
    }

    public boolean consumir(int idProducto, int cantidadAconsumir) {
        String sqlSelectLotes = "SELECT id_lote, cantidad_disponible FROM lotes_inventario "
                + "WHERE id_producto = ? AND cantidad_disponible > 0 "
                + "ORDER BY fecha_caducidad ASC";

        String sqlUpdateLote = "UPDATE lotes_inventario SET cantidad_disponible = ? WHERE id_lote = ?";
        String sqlDeleteLote = "DELETE FROM lotes_inventario WHERE id_lote = ?";

        return ejecutarConsumir(sqlSelectLotes, sqlUpdateLote, sqlDeleteLote, idProducto, cantidadAconsumir);
    }

    private PrioridadCola<LotesInventario> ejecutarSelectAll(String sql) {
        cola = new PrioridadCola<>(3);
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                LotesInventario l = new LotesInventario();
                l.setIdLote(rs.getInt("id_lote"));
                l.setIdProducto(rs.getInt("id_producto"));
                l.setProductoNombre(rs.getString("producto_nombre"));
                l.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                l.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                l.setFechaRecepcion(rs.getDate("fecha_recepcion").toLocalDate());
                l.calcularPrioridad();
                cola.offer(l, l.getPrioridad());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return cola;
    }

    private boolean ejecutarInsert(String sql, LotesInventario lote) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, lote.getIdProducto());
            ps.setInt(2, lote.getCantidadDisponible());
            ps.setDate(3, java.sql.Date.valueOf(lote.getFechaCaducidad()));
            ps.setDate(4, java.sql.Date.valueOf(lote.getFechaRecepcion()));

            int filas = ps.executeUpdate();
            if (filas == 0) {
                return false;
            }

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
        } finally {
            cerrarRecursos();
        }
    }

    private boolean ejecutarUpdate(String sql, LotesInventario lote) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, lote.getIdProducto());
            ps.setInt(2, lote.getCantidadDisponible());
            ps.setDate(3, java.sql.Date.valueOf(lote.getFechaCaducidad()));
            ps.setDate(4, java.sql.Date.valueOf(lote.getFechaRecepcion()));
            ps.setInt(5, lote.getIdLote());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                selectAll();
            }
            return ok;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    private LotesInventario ejecutarBuscar(String sql, int idLote) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idLote);
            rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return null;
    }

    private LotesInventario ejecutarBuscarPorNombre(String sql, String nombre) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return null;
    }

    private int ejecutarObtenerCantidad(String sql, int idProducto) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_disponible");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return 0;
    }

    private boolean ejecutarConsumir(String sqlSelect, String sqlUpdate, String sqlDelete, int idProducto, int cantidadAconsumir) {
        int cantidadRestante = cantidadAconsumir;
        Connection conn = null;

        try {
            conn = conectar.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, idProducto);
                try (ResultSet rsLocal = psSelect.executeQuery()) {
                    while (rsLocal.next() && cantidadRestante > 0) {
                        int idLote = rsLocal.getInt("id_lote");
                        int disponible = rsLocal.getInt("cantidad_disponible");

                        if (disponible <= cantidadRestante) {
                            cantidadRestante -= disponible;
                            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                                psDelete.setInt(1, idLote);
                                psDelete.executeUpdate();
                            }
                        } else {
                            int nuevaCantidad = disponible - cantidadRestante;
                            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
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
                    conectar.closeConexion(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cerrarRecursos() {
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
}
