/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jorge
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder = "";
    private boolean focused = false;
    private final Color lineColor = new Color(180, 180, 180);
    private final Color focusColor = new Color(51, 153, 255);
    private final int lineThickness = 2;
    private int animWidth = 0;       // ancho actual de la línea animada
    private Timer animTimer;         // controlador de animación
    private boolean animatingIn = false;

    public PlaceholderTextField() {

        setBorder(new EmptyBorder(5, 5, 10, 5));
        setSelectionColor(new Color(70, 130, 180, 102));
        initFocusAnimation();
        initTimer();

    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();

    }

    private void initFocusAnimation() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                focused = true;

                animatingIn = true;

                animTimer.start();

                repaint();

            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                animatingIn = false;
                animTimer.start();
                repaint();

            }

        });

    }

    private void initTimer() {
        animTimer = new Timer(15, e -> {
            int target = getWidth();
            if (animatingIn) {
                if (animWidth < target) {
                    animWidth += 20; // velocidad de expansión
                    if (animWidth > target) {
                        animWidth = target;
                    }
                    repaint();
                } else {
                    animTimer.stop();
                }

            } else {
                if (animWidth > 0) {
                    animWidth -= 20; // velocidad de contracción
                    if (animWidth < 0) {
                        animWidth = 0;
                    }
                    repaint();
                } else {
                    animTimer.stop();
                }

            }

        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Placeholder
        if (getText().isEmpty()) {
            g2.setColor(focused ? focusColor : Color.GRAY);
            FontMetrics fm = g2.getFontMetrics();
            int y = getHeight() / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(placeholder, getInsets().left, y);

        }

        // Línea base
        int yLine = getHeight() - 2;
        g2.setColor(lineColor);
        g2.fillRect(0, yLine - lineThickness + 1, getWidth(), lineThickness);

        // Línea animada
        g2.setColor(focusColor);
        g2.fillRect((getWidth() - animWidth) / 2, yLine - lineThickness + 1, animWidth, lineThickness);
        g2.dispose();

    }

}
