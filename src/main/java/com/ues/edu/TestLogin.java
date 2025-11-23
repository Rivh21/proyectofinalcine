/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.ues.edu.controlador.ControladorLogin;
import com.ues.edu.vista.Login;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author jorge
 */
public class TestLogin {

    public static void main(String[] args) {
        // Tema oscuro
         try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        Login login = new Login();
        ControladorLogin cl = new ControladorLogin(login);
        login.setVisible(true);

// Tema claro
//        try {
//            FlatLightLaf.setup();
//            UIManager.put("Panel.background", Color.WHITE);
//            UIManager.put("TextComponent.background", Color.WHITE);
//            UIManager.put("List.background", Color.WHITE);
//
//        } catch (Exception ex) {
//            System.err.println("Falló la configuración del tema.");
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            Login login = new Login();
//            ControladorLogin cl = new ControladorLogin(login);
//            login.setVisible(true);
//        });
    }

}
