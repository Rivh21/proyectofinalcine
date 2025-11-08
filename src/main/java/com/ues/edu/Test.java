/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.ues.edu;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.ues.edu.vista.Dashboard;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author jorge
 */
public class Test {

    public static void main(String[] args) {
        // Aplicar FlatLaf globalmente
//        try {
//            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
//        } catch (UnsupportedLookAndFeelException ex) {
//            ex.printStackTrace();
//        }
////Tema Oscuro
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        Dashboard db = new Dashboard();
        db.setVisible(true);

////Tema Claro
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
//            Dashboard db = new Dashboard();
//            db.setVisible(true);
//        });
    }
}
