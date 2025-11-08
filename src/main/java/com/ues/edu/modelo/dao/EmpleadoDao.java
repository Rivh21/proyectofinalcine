/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IEmpleado;
import com.ues.edu.modelo.Empleado;
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
public class EmpleadoDao implements IEmpleado {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public EmpleadoDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<Empleado> selectAll() {
        String sql = "select * from empleados";
        return select(sql);
    }

    @Override
    public ListaSimple<Empleado> selectAllTo(String atributo, String condicion) {
        String sql = "select * from empleados where " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimple<Empleado> buscar(String dato) {
        String sql = "select * from empleados where "
                + " nombre like '"
                + dato + "%' or telefono like'" + dato + "%'"
                + "or apellido like'" + dato + "%'"
                + "or salario like'" + dato + "%'"
                + "or email like'" + dato + "%'"
                + "or dui like'" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(Empleado obj) {
        if (existeDui(obj.getDui())) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Validación",
                    "El DUI " + obj.getDui() + " ya está registrado.",
                    DesktopNotify.ERROR, 3000);
            return false;
        }

        String sql = "insert into empleados(nombre, apellido, dui, email, telefono, salario) VALUES (?, ?, ?, ?, ?, ?)";
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean update(Empleado obj) {
        String sql = "update empleados set nombre=?, apellido=?, dui=?, email=?, telefono=?, salario=? where id_empleado=" + obj.getIdEmpleado();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Empleado obj) {
        String sql = "delete from empleados where id_empleado='" + obj.getIdEmpleado() + "'";
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

    private ListaSimple<Empleado> select(String sql) {
        ListaSimple<Empleado> lista = new ListaSimple();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Empleado obj = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Empleado();
                obj.setIdEmpleado(rs.getInt("id_empleado"));
                obj.setNombre(rs.getString("nombre"));
                obj.setApellido(rs.getString("apellido"));
                obj.setDui(rs.getString("dui"));
                obj.setEmail(rs.getString("email"));
                obj.setTelefono(rs.getString("telefono"));
                obj.setSalario(rs.getDouble("salario"));
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

    private boolean alterarRegistro(String sql, Empleado obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getApellido());
            ps.setString(3, obj.getDui());
            ps.setString(4, obj.getEmail());
            ps.setString(5, obj.getTelefono());
            ps.setDouble(6, obj.getSalario());
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

    @Override
    public boolean existeDui(String dui) {
        String sql = "SELECT COUNT(*) FROM empleados WHERE dui = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, dui);
            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar DUI en la BD: " + e.getMessage());
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return existe;
    }

    @Override
    public ListaSimple<Empleado> selectEmpleadosSinUsuario() {
        String sql = "SELECT e.* FROM empleados e "
                + "LEFT JOIN usuarios u ON e.id_empleado = u.id_empleado "
                + "WHERE u.id_empleado IS NULL";
        return select(sql);
    }

}
