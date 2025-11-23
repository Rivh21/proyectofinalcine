/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import com.ues.edu.interfaces.IAsiento;
import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.Sala;
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
public class AsientoDao implements IAsiento {

    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public AsientoDao() {
        conectar = new Conexion();
    }

    @Override
    public ListaSimple<Asiento> selectAll() {
        String sql = "SELECT a.id_asiento, a.fila, a.numero, s.id_sala, s.nombre_sala "
                + "FROM asientos a JOIN salas s ON a.id_sala = s.id_sala";
        return select(sql);
    }

    @Override
    public ListaSimple<Asiento> selectBySala(int idSala) {
        String sql = "SELECT a.id_asiento, a.fila, a.numero, s.id_sala, s.nombre_sala "
                + "FROM asientos a JOIN salas s ON a.id_sala = s.id_sala "
                + "WHERE a.id_sala = " + idSala;
        return select(sql);
    }

    @Override
    public boolean generate(int idSala, char filaInicio, int numFilas, int asientosPorFila) {
        String sql = "INSERT INTO asientos (id_sala, fila, numero) VALUES (?, ?, ?)";
        return generarRegistroMasivo(sql, idSala, filaInicio, numFilas, asientosPorFila);
    }

    @Override
    public boolean update(Asiento obj) {
        String sql = "UPDATE asientos SET fila=?, numero=? WHERE id_asiento=" + obj.getIdAsiento();
        return alterarRegistro(sql, obj);
    }

    @Override
    public boolean delete(Asiento obj) {
        String sql = "DELETE FROM asientos WHERE id_asiento='" + obj.getIdAsiento() + "'";
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error al Eliminar", "No se puede eliminar el asiento. Podría estar asignado a una venta (boleto).", DesktopNotify.ERROR, 3000);
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

    private ListaSimple<Asiento> select(String sql) {
        ListaSimple<Asiento> lista = new ListaSimple();
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sala sala = new Sala(rs.getInt("id_sala"), rs.getString("nombre_sala"));

                Asiento obj = new Asiento(
                        rs.getInt("id_asiento"),
                        sala,
                        rs.getString("fila"),
                        rs.getInt("numero")
                );
                lista.insertar(obj);
            }
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Consulta", "Error al cargar la lista de asientos.", DesktopNotify.ERROR, 3000);
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

    private boolean generarRegistroMasivo(String sql, int idSala, char filaInicio, int numFilas, int asientosPorFila) {
        PreparedStatement ps = null;
        char filaActual = filaInicio;

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);

            for (int i = 0; i < numFilas; i++) {
                for (int j = 1; j <= asientosPorFila; j++) {

                    ps.setInt(1, idSala);
                    ps.setString(2, String.valueOf(filaActual));
                    ps.setInt(3, j);

                    ps.addBatch();
                }
                filaActual++;
            }

            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Generación", "Error al crear asientos masivamente. Verifique si ya existen (Clave Única).", DesktopNotify.ERROR, 3000);
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

    private boolean alterarRegistro(String sql, Asiento obj) {
        PreparedStatement ps = null;
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getFila());
            ps.setInt(2, obj.getNumero());

            ps.execute();
            return true;
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error de Edición", "Error al editar el asiento. Verifique la unicidad.", DesktopNotify.ERROR, 3000);
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
    public boolean salaTieneAsientos(int idSala) {
        String sql = "SELECT 1 FROM asientos WHERE id_sala = ? LIMIT 1";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idSala);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }
}
