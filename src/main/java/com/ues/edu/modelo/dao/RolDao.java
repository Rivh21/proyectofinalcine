/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IRol;
import com.ues.edu.modelo.Rol;
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
public class RolDao implements IRol {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public RolDao() {
        conectar = new Conexion();
    }
    
    @Override
    public ListaSimple<Rol> selectAll() {
         String sql = "SELECT * FROM roles";
        return select(sql);
    }

    @Override
    public ListaSimple<Rol> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT * FROM roles WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimple<Rol> buscar(String dato) {
         String sql = "SELECT * FROM roles WHERE "
                + " nombre_rol LIKE '"  + dato + "%'"
                + "OR permisos LIKE'" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(Rol obj) {
        String sql = "INSERT INTO roles(nombre_rol, permisos) VALUES (?, ?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(Rol obj) {
       String sql = "UPDATE roles SET nombre_rol=?, permisos=? WHERE id_rol=" + obj.getIdRol();
       return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Rol obj) {
        String sql = "DELETE FROM roles WHERE id_rol='" + obj.getIdRol() + "'";
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

    private ListaSimple<Rol> select(String sql) {
        ListaSimple<Rol> lista = new ListaSimple();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Rol obj = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Rol();
                obj.setIdRol(rs.getInt("id_rol"));
                obj.setNombreRol(rs.getString("nombre_rol"));
                obj.setPermisos(rs.getString("permisos"));
                lista.insertar(obj);
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
                conectar.closeConexion(con);
            } catch (SQLException e) {
            }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, Rol obj) {
      PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombreRol());
            ps.setString(2, obj.getPermisos());
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

}
