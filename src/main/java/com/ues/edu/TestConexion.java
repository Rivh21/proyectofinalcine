/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu;

import com.ues.edu.modelo.dao.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author jorge
 */
public class TestConexion {
    public static void main(String[] args) {
        Conexion conexion = new Conexion();
        Connection cn = null;

        try {
            cn = conexion.getConexion();

            if (cn != null && !cn.isClosed()) {
                System.out.println("¡Conexión exitosa a la base de datos!");
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la conexión: " + e.getMessage());
        } finally {
            conexion.closeConexion(cn);
            System.out.println("Conexión cerrada.");
        }
    }
}
