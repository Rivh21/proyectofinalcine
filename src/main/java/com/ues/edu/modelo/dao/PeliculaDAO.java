package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IPelicula;
import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.sql.*;
import java.util.ArrayList;

public class PeliculaDAO implements IPelicula {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public PeliculaDAO() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimpleCircular<Pelicula> selectAll() {
        String sql = "SELECT * FROM peliculas";
        return select(sql);
    }

    @Override
    public ListaSimpleCircular<Pelicula> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT * FROM peliculas WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimpleCircular<Pelicula> buscar(String dato) {
        String sql = "SELECT * FROM peliculas WHERE "
                + " titulo LIKE '" + dato + "%'"
                + " OR genero LIKE '" + dato + "%'"
                + " OR clasificacion LIKE '" + dato + "%'"
                + " OR duracion_minutos LIKE '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(Pelicula obj) {
        String sql = "INSERT INTO peliculas(titulo, duracion_minutos, genero, clasificacion) VALUES (?, ?, ?, ?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(Pelicula obj) {
        String sql = "UPDATE peliculas SET titulo=?, duracion_minutos=?, genero=?, clasificacion=? WHERE id_pelicula=" + obj.getId_pelicula();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Pelicula obj) {
        String sql = "DELETE FROM peliculas WHERE id_pelicula='" + obj.getId_pelicula() + "'";
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error en sql", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private ListaSimpleCircular<Pelicula> select(String sql) {
        ListaSimpleCircular<Pelicula> lista = new ListaSimpleCircular<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pelicula p = new Pelicula();
                p.setId_pelicula(rs.getInt("id_pelicula"));
                p.setTitulo(rs.getString("titulo"));
                p.setDuracion_minutos(rs.getInt("duracion_minutos"));
                p.setGenero(rs.getString("genero"));
                p.setClasificacion(rs.getString("clasificacion"));
                lista.insertar(p);
            }

        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error en sql", DesktopNotify.ERROR, 3000);
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
            }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, Pelicula obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getTitulo());
            ps.setInt(2, obj.getDuracion_minutos());
            ps.setString(3, obj.getGenero());
            ps.setString(4, obj.getClasificacion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error en sql", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conectar.closeConexion(con);
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public Pelicula buscarPorId(int id) {
        String sql = "SELECT * FROM peliculas WHERE id_pelicula=" + id;
        ListaSimpleCircular<Pelicula> lista = select(sql);

        ArrayList arr = lista.toArray();
        if (arr.size() > 0) {
            return (Pelicula) arr.get(0);
        }
        return null;
    }

}
