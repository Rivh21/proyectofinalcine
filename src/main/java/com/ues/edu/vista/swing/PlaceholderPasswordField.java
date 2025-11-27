/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jorge
 */
public class PlaceholderPasswordField extends JPasswordField{
    private String placeholder = "";
    private boolean focused = false;
    private final Color lineColor = new Color(180, 180, 180);
    private final Color focusColor = new Color(51, 153, 255);
    private final int lineThickness = 2;
    private int animWidth = 0;
    private Timer animTimer;
    private boolean animatingIn = false;
    private boolean passwordVisible = false;
    private final char defaultEchoChar;

    public PlaceholderPasswordField() {
        // Aumenta el borde derecho para dar espacio al ícono del ojo
        setBorder(new EmptyBorder(5, 5, 10, 5));
        setSelectionColor(new Color(70, 130, 180, 102));
        setTransferHandler(null);

        // Guarda el carácter de eco por defecto
        this.defaultEchoChar = getEchoChar();

        initFocusAnimation();
        initTimer();
        initShowHidePassword();
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


    private void initShowHidePassword() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Rectangle iconBounds = getIconBounds();
                if (iconBounds.contains(e.getPoint())) {
                    togglePasswordVisibility();
                }
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (getIconBounds().contains(e.getPoint())) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            // Muestra la contraseña
            setEchoChar((char) 0);
        } else {
            // Oculta la contraseña con el carácter por defecto
            setEchoChar(defaultEchoChar);
        }
        repaint();
    }
    
    /**
     * Calcula la posición y tamaño del ícono del ojo.
     * @return 
     */
    private Rectangle getIconBounds() {
        Insets insets = getInsets();
        int iconSize = 16;
        int x = getWidth() - insets.right - iconSize;
        int y = (getHeight() - iconSize) / 2;
        return new Rectangle(x, y, iconSize, iconSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getPassword().length == 0) {
            g2.setColor(focused ? focusColor : Color.GRAY);
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left, y);
        }

        // Línea base
        int yLine = getHeight() - 2;
        g2.setColor(lineColor);
        g2.fillRect(0, yLine - lineThickness + 1, getWidth(), lineThickness);

        // Línea animada
        g2.setColor(focusColor);
        g2.fillRect((getWidth() - animWidth) / 2, yLine - lineThickness + 1, animWidth, lineThickness);
        
        // Dibuja el ícono de mostrar/ocultar contraseña
        drawPasswordIcon(g2);
        
        g2.dispose();
    }
    

    private void drawPasswordIcon(Graphics2D g2) {
        Rectangle icon = getIconBounds();
        g2.setColor(Color.GRAY);
        
        // Dibuja el contorno del ojo (un arco)
        g2.drawArc(icon.x, icon.y + icon.height / 4, icon.width, icon.height / 2, 0, 180);
        g2.drawArc(icon.x, icon.y + icon.height / 4, icon.width, icon.height / 2, 0, -180);
        // Dibuja la pupila
        g2.fillOval(icon.x + icon.width / 2 - 2, icon.y + icon.height / 2 - 2, 4, 4);

        if (passwordVisible) {
            // Ojo abierto
        } else {
            // Dibuja una línea diagonal para tachar el ojo
            g2.drawLine(icon.x, icon.y + icon.height, icon.x + icon.width, icon.y);
        }
    }
}
