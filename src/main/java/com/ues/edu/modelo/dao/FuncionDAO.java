package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IFuncion;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import java.sql.*;
import java.time.ZoneId;

public class FuncionDAO implements IFuncion {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public FuncionDAO() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimpleCircular<Funcion> selectAll() {
        String sql = "SELECT f.id_funcion, f.id_pelicula, p.titulo AS pelicula, " +
                     "f.id_sala, s.nombre_sala AS sala, " +
                     "f.fecha_hora_inicio, f.precio_boleto " +
                     "FROM funciones f " +
                     "JOIN peliculas p ON f.id_pelicula = p.id_pelicula " +
                     "JOIN salas s ON f.id_sala = s.id_sala " +
                     "ORDER BY f.fecha_hora_inicio ASC";
        return select(sql);
    }

    @Override
    public ListaSimpleCircular<Funcion> buscar(String textoBusqueda) {
        String sql = "SELECT f.id_funcion, f.id_pelicula, p.titulo AS pelicula, " +
                     "f.id_sala, s.nombre_sala AS sala, " +
                     "f.fecha_hora_inicio, f.precio_boleto " +
                     "FROM funciones f " +
                     "JOIN peliculas p ON f.id_pelicula = p.id_pelicula " +
                     "JOIN salas s ON f.id_sala = s.id_sala " +
                     "WHERE p.titulo LIKE '%" + textoBusqueda + "%' " +
                     "OR s.nombre_sala LIKE '%" + textoBusqueda + "%' " +
                     "ORDER BY f.fecha_hora_inicio ASC";
        return select(sql);
    }

    @Override
    public boolean insert(Funcion obj) {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_hora_inicio, precio_boleto) VALUES (?, ?, ?, ?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(Funcion obj) {
        String sql = "UPDATE funciones SET id_pelicula=?, id_sala=?, fecha_hora_inicio=?, precio_boleto=? WHERE id_funcion=" + obj.getId_funcion();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Funcion obj) {
        String sql = "DELETE FROM funciones WHERE id_funcion='" + obj.getId_funcion() + "'";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la función", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Funcion buscarPorId(int id) {
        String sql = "SELECT f.id_funcion, f.id_pelicula, p.titulo AS pelicula, " +
                     "f.id_sala, s.nombre_sala AS sala, " +
                     "f.fecha_hora_inicio, f.precio_boleto " +
                     "FROM funciones f " +
                     "JOIN peliculas p ON f.id_pelicula = p.id_pelicula " +
                     "JOIN salas s ON f.id_sala = s.id_sala " +
                     "WHERE f.id_funcion='" + id + "'";
        ListaSimpleCircular<Funcion> lista = select(sql);
        Funcion obj = null;
        for (Funcion f : lista.toArray()) { // usamos toArray para obtener el primer elemento
            obj = f;
            break;
        }
        return obj;
    }

    private ListaSimpleCircular<Funcion> select(String sql) {
        ListaSimpleCircular<Funcion> lista = new ListaSimpleCircular<>();
        Funcion obj;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Funcion();
                obj.setId_funcion(rs.getInt("id_funcion"));
                obj.setId_pelicula(rs.getInt("id_pelicula"));
                obj.setPeliculaTitulo(rs.getString("pelicula"));
                obj.setId_sala(rs.getInt("id_sala"));
                obj.setSalaNombre(rs.getString("sala"));

                Timestamp ts = rs.getTimestamp("fecha_hora_inicio");
                if (ts != null) obj.setFecha_hora_inicio(ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                obj.setPrecio_boleto(rs.getDouble("precio_boleto"));

                lista.insertar(obj);
            }
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error en SQL", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, Funcion obj) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId_pelicula());
            ps.setInt(2, obj.getId_sala());
            ps.setTimestamp(3, Timestamp.valueOf(obj.getFecha_hora_inicio()));
            ps.setDouble(4, obj.getPrecio_boleto());
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error en SQL", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
