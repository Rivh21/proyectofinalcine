/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jorge
 */
public class Conexion {
     Connection cn;

    public Conexion() {
    }
    
    public Connection getConexion() {       
        String user = "root";
        String clave = "0000"; //cambiar contraseña
        String url = "jdbc:mariadb://localhost:3306/proyecto_cine";
        try {              
            this.cn = DriverManager.getConnection(url, user, clave);
        } catch (Exception ex) {
        }
        return cn;
    }

    public void closeConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
            
        }
    }
    
}
