/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jorge
 */
public class BuscadorTexto extends JTextField {

    private final String hint = "Buscar...";
    private final Image icono;
    private final Color lineColor = new Color(180, 180, 180);
    private final Color focusColor = new Color(51, 153, 255);
    private final int lineThickness = 2;
    private int animWidth = 0; // Ancho actual de la línea animada
    private Timer animTimer;   // Controlador de animación
    private boolean animatingIn = false;

    public BuscadorTexto() {

        // Espacio para el icono + línea inferior
        setBorder(new EmptyBorder(5, 35, 10, 5));
        setSelectionColor(new Color(70, 130, 180, 102));
        // Cargar icono
        icono = new ImageIcon(getClass().getResource("/iconos/buscador.png")).getImage();

        // --- Animación de línea ---
        animTimer = new Timer(15, e -> {

            int target = getWidth();

            if (animatingIn) {

                if (animWidth < target) {
                    animWidth += 20; // velocidad de expansión
                    repaint();

                } else {
                    animWidth = target;
                    ((Timer) e.getSource()).stop();

                }

            } else {

                if (animWidth > 0) {
                    animWidth -= 20; // velocidad de contracción
                    repaint();

                } else {
                    animWidth = 0;
                    ((Timer) e.getSource()).stop();

                }

            }

        });

        // Escuchamos los eventos de foco
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                animatingIn = true;
                animTimer.start();

            }

            @Override
            public void focusLost(FocusEvent e) {
                animatingIn = false;
                animTimer.start();

            }

        });

    }

    @Override

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Dibuja el icono ---
        if (icono != null) {
            int iconSize = 25;
            int y = (getHeight() - iconSize) / 2;
            g2.drawImage(icono, 7, y, iconSize, iconSize, this);

        }

        // --- Dibuja el hint ---
        if (getText().isEmpty()) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int h = getHeight();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = (((c0 & m) >>> 1) + ((c1 & m) >>> 1));
            g2.setColor(new Color(c2, true));
            g2.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);

        }

        // --- Línea inferior ---
        int yLine = getHeight() - 2;

        // Línea base (gris)
        g2.setColor(lineColor);
        g2.fillRect(0, yLine - lineThickness + 1, getWidth(), lineThickness);

        // Línea animada (azul)
        g2.setColor(focusColor);
        g2.fillRect((getWidth() - animWidth) / 2, yLine - lineThickness + 1, animWidth, lineThickness);

        g2.dispose();

    }
}
