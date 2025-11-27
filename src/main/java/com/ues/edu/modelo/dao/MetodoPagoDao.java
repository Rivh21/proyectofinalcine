/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IMetodoPago;
import com.ues.edu.modelo.MetodoPago;
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
public class MetodoPagoDao implements IMetodoPago {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public MetodoPagoDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<MetodoPago> selectAll() {
        String sql = "SELECT * FROM metodo_pago";
        return select(sql);
    }

    @Override
    public ListaSimple<MetodoPago> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT * FROM metodo_pago WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimple<MetodoPago> buscar(String dato) {
        String sql = "SELECT * FROM metodo_pago WHERE "
                + " nombre_metodo like '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(MetodoPago obj) {
        String sql = "INSERT INTO metodo_pago(nombre_metodo) VALUES (?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(MetodoPago obj) {
        String sql = "UPDATE metodo_pago SET nombre_metodo=? WHERE id_metodo_pago=" + obj.getidMetodoPago();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(MetodoPago obj) {
          String sql = "DELETE FROM metodo_pago WHERE id_metodo_pago='" + obj.getidMetodoPago()+ "'";
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

    private ListaSimple<MetodoPago> select(String sql) {
       ListaSimple<MetodoPago> lista = new ListaSimple();
        PreparedStatement ps = null;
        ResultSet rs = null;
        MetodoPago obj = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new MetodoPago();
                obj.setidMetodoPago(rs.getInt("id_metodo_pago"));
                obj.setnombreMetodo(rs.getString("nombre_metodo"));
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

    private boolean alterarRegistro(String sql, MetodoPago obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getnombreMetodo());
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
