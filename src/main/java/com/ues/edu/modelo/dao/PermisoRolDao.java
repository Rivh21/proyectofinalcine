/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IPermisoRol;
import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.PermisoRol;
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
public class PermisoRolDao implements IPermisoRol {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public PermisoRolDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<PermisoRol> selectAll() {
        String sql = "SELECT pr.id_permiso_rol, pr.tiene_permiso, r.id_rol, r.nombre_rol, p.id_permiso, p.nombre_permiso "
                + "FROM permiso_rol pr "
                + "JOIN roles r ON pr.id_rol = r.id_rol "
                + "JOIN permisos p ON pr.id_permiso = p.id_permiso";
        return select(sql);
    }

    @Override
    public ListaSimple<PermisoRol> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT pr.id_permiso_rol, pr.tiene_permiso, r.id_rol, r.nombre_rol, p.id_permiso, p.nombre_permiso "
                + "FROM permiso_rol pr "
                + "JOIN roles r ON pr.id_rol = r.id_rol "
                + "JOIN permisos p ON pr.id_permiso = p.id_permiso "
                + "WHERE " + atributo + " LIKE '" + condicion + "%'";
        return select(sql);
    }

    @Override
    public ListaSimple<PermisoRol> buscar(String dato) {
        String sql = "SELECT pr.id_permiso_rol, pr.tiene_permiso, r.id_rol, r.nombre_rol, p.id_permiso, p.nombre_permiso "
                + "FROM permiso_rol pr "
                + "JOIN roles r ON pr.id_rol = r.id_rol "
                + "JOIN permisos p ON pr.id_permiso = p.id_permiso "
                + "WHERE r.nombre_rol LIKE '" + dato + "%' OR p.nombre_permiso LIKE '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(PermisoRol obj) {
        String sql = "INSERT INTO permiso_rol(id_rol, id_permiso, tiene_permiso) VALUES (?, ?, ?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(PermisoRol obj) {
        String sql = "UPDATE permiso_rol SET tiene_permiso=? WHERE id_permiso_rol=" + obj.getIdPermisoRol();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(PermisoRol obj) {
        String sql = "DELETE FROM permiso_rol WHERE id_permiso_rol='" + obj.getIdPermisoRol() + "'";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al eliminar la relación permiso-rol.", DesktopNotify.ERROR, 3000);
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

    private ListaSimple<PermisoRol> select(String sql) {
        ListaSimple<PermisoRol> lista = new ListaSimple();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));
                Permiso permiso = new Permiso(rs.getInt("id_permiso"), rs.getString("nombre_permiso"));

                PermisoRol pr = new PermisoRol(
                        rs.getInt("id_permiso_rol"),
                        rol,
                        permiso,
                        rs.getBoolean("tiene_permiso")
                );
                lista.insertar(pr);
            }

        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Consulta M:M", "Error al cargar la lista de relaciones Permiso/Rol.", DesktopNotify.ERROR, 3000);
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
                e.printStackTrace();
            }
        }
        return lista;
    }

    private boolean alterarRegistro(String sql, PermisoRol obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);

            if (sql.contains("INSERT")) {
                ps.setInt(1, obj.getRol().getIdRol());
                ps.setInt(2, obj.getPermiso().getIdPermiso());
                ps.setBoolean(3, obj.getTienePermiso());

            } else if (sql.contains("UPDATE")) {
                ps.setBoolean(1, obj.getTienePermiso());
            }

            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Guardado", "Error al guardar o actualizar la relación.", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conectar.closeConexion(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
