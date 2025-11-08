/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IUsuario;
import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.Usuario;
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
public class UsuarioDao implements IUsuario {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public UsuarioDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<Usuario> selectAll() {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, e.id_empleado, e.nombre, e.apellido, r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN empleados e ON u.id_empleado = e.id_empleado "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol";
        return select(sql);
    }

    @Override
    public ListaSimple<Usuario> selectAllTo(String atributo, String condicion) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, e.id_empleado, e.nombre, e.apellido, r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN empleados e ON u.id_empleado = e.id_empleado "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE " + atributo + "='" + condicion + "'";
        return select(sql);
    }

    @Override
    public ListaSimple<Usuario> buscar(String dato) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, e.id_empleado, e.nombre, e.apellido, r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN empleados e ON u.id_empleado = e.id_empleado "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE e.nombre LIKE '" + dato + "%'"
                + "OR e.apellido LIKE '" + dato + "%'"
                + "OR r.nombre_rol LIKE '" + dato + "%'";
        return select(sql);
    }

    @Override
    public boolean insert(Usuario obj) {
        String sql = "INSERT INTO usuarios(id_rol, id_empleado, nombre_usuario, password)VALUES(?,?,?,?)";
        return insertarRegistro(sql, obj);
    }

    @Override
    public boolean update(Usuario obj) {
        String sql = "UPDATE usuarios SET id_rol=?, id_empleado=?, nombre_usuario=?, password=? "
                + "WHERE id_usuario=" + obj.getIdUsuario();
        return editarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Usuario obj) {
        String sql = "delete from usuarios where id_usuario='" + obj.getIdUsuario() + "'";
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

    @Override
    public Usuario login(String usuario, String claveEncriptada) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, e.id_empleado, e.nombre, e.apellido, r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN empleados e ON u.id_empleado = e.id_empleado "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol"
                + "WHERE u.nombre_usuario = ? AND u.password = ?";
        return selectLogin(sql, usuario, claveEncriptada);
    }

    private ListaSimple<Usuario> select(String sql) {
        ListaSimple<Usuario> lista = new ListaSimple();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario obj = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Usuario();
                obj.setIdUsuario(rs.getInt("id_usuario"));
                obj.setNombreUsuario(rs.getString("nombre_usuario"));

                try {
                    // Si la columna 'password' existe en el ResultSet (solo en la edición) se mapea
                    obj.setPassword(rs.getString("password"));
                } catch (SQLException ignore) {
                    // Si la columna NO existe (en selectAll), el valor queda como null, lo cual es seguro.
                }

                Empleado empleado = new Empleado();
                empleado.setIdEmpleado(rs.getInt("id_empleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
                obj.setEmpleado(empleado);

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                obj.setRol(rol);

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
                e.printStackTrace();
            }
        }
        return lista;
    }

    private boolean insertarRegistro(String sql, Usuario obj) {
        try {
            con = conectar.getConexion();
            PreparedStatement psUsuario = con.prepareStatement(sql);
            con.setAutoCommit(false);

            psUsuario.setInt(1, obj.getRol().getIdRol());
            psUsuario.setInt(2, obj.getEmpleado().getIdEmpleado());
            psUsuario.setString(3, obj.getNombreUsuario());
            psUsuario.setString(4, obj.getPassword());

            int filasAfectadas = psUsuario.executeUpdate();
            con.commit();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
            }
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al insertar usuario: " + e.getMessage(), DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            conectar.closeConexion(con);
        }
        return false;

    }

    private boolean editarRegistro(String sql, Usuario obj) {
        PreparedStatement psUsuario = null;
        try {
            con = conectar.getConexion();
            psUsuario = con.prepareStatement(sql);
            con.setAutoCommit(false);

            psUsuario.setInt(1, obj.getRol().getIdRol());
            psUsuario.setInt(2, obj.getEmpleado().getIdEmpleado());
            psUsuario.setString(3, obj.getNombreUsuario());
            psUsuario.setString(4, obj.getPassword());

            int filasAfectadas = psUsuario.executeUpdate();
            con.commit();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al actualizar usuario: " + e.getMessage(), DesktopNotify.ERROR, 3000);
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (psUsuario != null) {
                    psUsuario.close();
                }
                if (con != null) {
                    conectar.closeConexion(con);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Usuario selectLogin(String sql, String usuario, String claveEncriptada) {
        Usuario user = null;
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, claveEncriptada);

            rs = ps.executeQuery();
            if (rs.next()) {
                user = new Usuario();

                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombreUsuario(rs.getString("nombre_usuario"));
                user.setPassword(rs.getString("password"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                user.setRol(rol);

                Empleado empleado = new Empleado();
                empleado.setIdEmpleado(rs.getInt("id_empleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApellido(rs.getString("apellido"));
                empleado.setDui(rs.getString("dui"));
                empleado.setEmail(rs.getString("email"));
                empleado.setTelefono(rs.getString("telefono"));
                empleado.setSalario(rs.getDouble("salario"));
                user.setEmpleado(empleado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    conectar.closeConexion(con);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }

    public Usuario selectUsuarioParaEdicion(int idUsuario) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.password, "
                + "e.id_empleado, e.nombre, e.apellido, "
                + "r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "LEFT JOIN empleados e ON u.id_empleado = e.id_empleado " // LEFT JOIN para empleados opcionales
                + "WHERE u.id_usuario = " + idUsuario;

        // Llama al método base 'select' que ahora es capaz de mapear el password
        ListaSimple<Usuario> lista = select(sql);

        // Devuelve el objeto Usuario completo (con el hash de password)
        return lista.isEmpty() ? null : lista.get(0);
    }
}
