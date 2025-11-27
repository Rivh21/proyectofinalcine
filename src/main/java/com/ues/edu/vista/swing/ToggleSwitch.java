/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;
import javax.swing.Timer;

/**
 *
 * @author jorge
 */
public class ToggleSwitch extends JToggleButton {

    private float location = 0f; // 0 = Izquierda, 1 = Derecha
    private Timer timer;

    public ToggleSwitch() {
        setBorder(null);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(50, 25);
        
        // Se ejecuta cada 10ms para mover la bolita suavemente
        timer = new Timer(10, e -> {
            boolean shouldRepaint = false;

            if (isSelected()) {
                // Si está activado, mover hacia la derecha (1.0)
                if (location < 1f) {
                    location += 0.1f; // Velocidad de animación
                    if (location > 1f) {
                        location = 1f;
                    }
                    shouldRepaint = true;
                }
            } else {
                // Si está desactivado, mover hacia la izquierda (0.0)
                if (location > 0f) {
                    location -= 0.1f; // Velocidad de animación
                    if (location < 0f) {
                        location = 0f;
                    }
                    shouldRepaint = true;
                }
            }

            if (shouldRepaint) {
                repaint();
            } else {
                // Si ya llegó al destino, para el timer para ahorrar recursos
                timer.stop();
            }
        });

        // Escuchar el cambio de estado
        addActionListener(e -> {
            timer.start();
        });

        // Redondear bordes en FlatLaf
        putClientProperty(FlatClientProperties.STYLE, "arc: 999");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int size = height - 4; // Tamaño de la bolita

        // Dibujar fondo (Píldora)
        if (isSelected()) {
            g2.setColor(new Color(230, 230, 230)); // Fondo Claro
        } else {
            g2.setColor(new Color(80, 80, 80));    // Fondo Oscuro
        }
        g2.fillRoundRect(0, 0, width, height, height, height);

        // Calcular posición animada
        double endLocation = width - size - 2;
        double startLocation = 2;
        double x = startLocation + (location * (endLocation - startLocation));

        // Dibujar la bolita (Círculo)
        if (location > 0.5f) { // Si está más a la derecha
            g2.setColor(new Color(255, 200, 0)); // Naranja (Sol)
        } else {
            g2.setColor(Color.WHITE);  // Blanco (Luna)
        }
        g2.fillOval((int) x, 2, size, size);
    }
}
