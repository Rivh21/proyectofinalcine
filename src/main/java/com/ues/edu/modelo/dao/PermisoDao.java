/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IPermiso;
import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.estructuras.ListaSimple;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author radon
 */
public class PermisoDao implements IPermiso {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public PermisoDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<Permiso> selectAll() {
        String sql = "SELECT id_permiso, nombre_permiso FROM permisos";
        return select(sql);
    }

    @Override
    public ListaSimple<Permiso> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT id_permiso, nombre_permiso FROM permisos WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimple<Permiso> buscar(String dato) {
        String sql = "SELECT id_permiso, nombre_permiso FROM permisos WHERE nombre_permiso LIKE '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(Permiso obj) {
        String sql = "INSERT INTO permisos(nombre_permiso) VALUES (?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(Permiso obj) {
        String sql = "UPDATE permisos SET nombre_permiso=? WHERE id_permiso=" + obj.getIdPermiso();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Permiso obj) {
        String sql = "DELETE FROM permisos WHERE id_permiso='" + obj.getIdPermiso() + "'";
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error al Eliminar", "No se puede eliminar el permiso. Está asignado a uno o más roles.", DesktopNotify.ERROR, 3000);
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

    private ListaSimple<Permiso> select(String sql) {
        ListaSimple<Permiso> lista = new ListaSimple<>();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Permiso p = new Permiso();
                p.setIdPermiso(rs.getInt("id_permiso"));
                p.setNombrePermiso(rs.getString("nombre_permiso"));
                lista.insertar(p);
            }
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Consulta", "Error al cargar la lista de permisos.", DesktopNotify.ERROR, 3000);
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

    private boolean alterarRegistro(String sql, Permiso obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombrePermiso());
            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Guardado", "Error al guardar o actualizar el permiso. Verifique el nombre.", DesktopNotify.ERROR, 3000);
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

    @Override
    public ListaSimple<Permiso> selectAllPermisos() {
        return select("SELECT id_permiso, nombre_permiso FROM permisos");
    }

    public ListaSimple<Permiso> obtenerIdPorNombre(String nombrePermiso) {
        String sql = "SELECT id_permiso, nombre_permiso FROM permisos WHERE nombre_permiso = ?";
        return ejecutarObtenerIdPorNombre(sql, nombrePermiso);
    }

    private ListaSimple<Permiso> ejecutarObtenerIdPorNombre(String sql, String nombrePermiso) {
        ListaSimple<Permiso> lista = new ListaSimple<>();

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombrePermiso);
            rs = ps.executeQuery();

            while (rs.next()) {
                Permiso p = new Permiso();
                p.setIdPermiso(rs.getInt("id_permiso"));
                p.setNombrePermiso(rs.getString("nombre_permiso"));
                lista.insertar(p);
            }
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage(
                    "Error de Consulta",
                    "No se pudo obtener el permiso por nombre.",
                    DesktopNotify.ERROR,
                    3000
            );
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
}
