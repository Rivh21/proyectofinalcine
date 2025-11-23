/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jorge
 */
public class BotonGradient extends JButton {

    private final Color color1 = new Color(0, 204, 255); // Cyan del icono
    private final Color color2 = new Color(102, 51, 255); // Morado del icono
    private float alpha = 1.0f; // Para efecto al presionar

    public BotonGradient() {
        setContentAreaFilled(false); // Quitar relleno por defecto
        setForeground(Color.WHITE); // Texto blanco
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manita al pasar mouse
        setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding interno
        setFocusPainted(false); // Quitar linea punteada de foco
        setFont(new Font("Roboto", Font.BOLD, 14)); // Fuente negrita

        // Eventos para animacion simple
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                alpha = 0.9f; // Un poco transparente al pasar mouse
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alpha = 1.0f; // Normal al salir
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent me) {
                alpha = 0.8f; // Más transparente al presionar
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                alpha = 0.9f;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Crear el degradado
        int width = getWidth();
        int height = getHeight();
        GradientPaint gra = new GradientPaint(0, 0, color1, width, 0, color2);
        
        // Aplicar transparencia para efectos de mouse
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        g2.setPaint(gra);
        // Dibujar rectangulo redondeado (radio 20)
        g2.fillRoundRect(0, 0, width, height, 20, 20);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
