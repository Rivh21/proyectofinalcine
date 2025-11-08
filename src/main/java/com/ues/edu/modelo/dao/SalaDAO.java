package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.ISala;
import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import java.sql.*;

public class SalaDAO implements ISala {

    private Conexion conectar;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public SalaDAO() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimpleCircular<Sala> selectAll() {
        String sql = "SELECT * FROM salas ORDER BY nombre_sala ASC";
        return select(sql);
    }

    @Override
    public ListaSimpleCircular<Sala> selectAllTo(String atributo, String valor) {
        String sql = "SELECT * FROM salas WHERE " + atributo + "=? ORDER BY nombre_sala ASC";
        ListaSimpleCircular<Sala> lista = new ListaSimpleCircular<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, valor);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sala s = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));
                lista.insertar(s);
            }
        } catch (SQLException e) {
            System.err.println("Error selectAllTo salas: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    @Override
    public ListaSimpleCircular<Sala> buscar(String textoBusqueda) {
        String sql = "SELECT * FROM salas WHERE nombre_sala LIKE ? ORDER BY nombre_sala ASC";
        ListaSimpleCircular<Sala> lista = new ListaSimpleCircular<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Sala s = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));
                lista.insertar(s);
            }
        } catch (SQLException e) {
            System.err.println("Error buscar salas: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    @Override
    public boolean insert(Sala sala) {
        String sql = "INSERT INTO salas(nombre_sala) VALUES(?)";
        return alterarRegistro(sql, sala);
    }

    @Override
    public boolean update(Sala sala) {
        String sql = "UPDATE salas SET nombre_sala=? WHERE id_sala=?";
        return alterarRegistro(sql, sala);
    }

    @Override
    public boolean delete(Sala sala) {
        String sql = "DELETE FROM salas WHERE id_sala=?";
        return alterarRegistro(sql, sala);
    }

    @Override
    public Sala buscarPorId(int idSala) {
        String sql = "SELECT * FROM salas WHERE id_sala=?";
        Sala s = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idSala);
            rs = ps.executeQuery();
            if (rs.next()) {
                s = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorId sala: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
        return s;
    }

    @Override
    public Sala buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM salas WHERE nombre_sala=?";
        Sala s = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                s = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorNombre sala: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
        return s;
    }

    private ListaSimpleCircular<Sala> select(String sql) {
        ListaSimpleCircular<Sala> lista = new ListaSimpleCircular<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sala s = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));
                lista.insertar(s);
            }
        } catch (SQLException e) {
            System.err.println("Error select salas: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, Sala sala) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, sala.getNombre_sala());
            if (sql.contains("UPDATE") || sql.contains("DELETE")) {
                ps.setInt(2, sala.getId_sala());
            }
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error alterarRegistro sala: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); conectar.closeConexion(con); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
