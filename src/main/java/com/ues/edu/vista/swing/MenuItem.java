/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ues.edu.vista.swing;

import com.ues.edu.modelo.ModeloMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;

/**
 *
 * @author jorge
 */
public class MenuItem extends javax.swing.JPanel {

    private boolean seleccionado;
    private boolean isHover;
    private final ModeloMenu data;
    private static final Color SUBMENU_BACKGROUND = new Color(0, 0, 0, 80);

    public MenuItem(ModeloMenu dato) {
        initComponents();
        setOpaque(false);

        this.data = dato;
        setOpaque(false);

        lbIcono.setOpaque(false);
        lbNombre.setOpaque(false);
        lbNombre.setForeground(Color.WHITE);
//        lbNombre.setForeground(Color.LIGHT_GRAY);

        lbIcono.setPreferredSize(new Dimension(0, 0));
        lbIcono.setMaximumSize(new Dimension(0, 0));

        // Llamada a la nueva lógica de estilos
        applyStyle();
    }

//    private void applyStyle() {
//        int paddingLeft = 15;
//        if (data.getTipo() == ModeloMenu.TipoMenu.TITULO) {
//
//            lbIcono.setVisible(false);
//            lbNombre.setText(data.getNombre());
//            lbNombre.setFont(new Font("roboto", Font.BOLD, 12));
//            lbNombre.setVisible(true);
//            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
//
//        } else if (data.getTipo() == ModeloMenu.TipoMenu.VACIO || data.getNombre().trim().isEmpty()) {
//            // VACIO: Sin contenido
//            lbIcono.setVisible(false);
//            lbNombre.setText(" ");
//            lbNombre.setVisible(true);
//            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
//        } else {
//            if (data.isSubMenuChild()) {
//                paddingLeft = 30;
//            }
//
//            setBorder(BorderFactory.createEmptyBorder(2, paddingLeft, 2, 5));
//
//            lbIcono.setVisible(true);
//            lbNombre.setFont(new Font("roboto", Font.PLAIN, 12));
//
//            String nombre = data.getNombre();
//
//            if (data.getTipo() == ModeloMenu.TipoMenu.SUB_MENU) {
//                // 3. ÍTEM PADRE (SUB_MENU): Añadir flecha
//                if (data.isExpandible()) {
//                    nombre = nombre + "  \u25BC"; // Flecha abajo (Expandido)
//                } else {
//                    nombre = nombre + "  \u25B6"; // Flecha derecha (Contraído)
//                }
//                lbIcono.setIcon(data.toIcon());
//
//            } else {
//                // ÍTEM NORMAL (MENÚ o SUBMENÚ HIJO)
//                lbIcono.setIcon(data.toIcon());
//            }
//
//            lbNombre.setText(nombre);
//            lbNombre.setVisible(true);
//        }
//    }
    private void applyStyle() {
        int paddingLeft = 15;
        String textoHtml;

        if (data.getTipo() == ModeloMenu.TipoMenu.TITULO) {
            lbIcono.setVisible(false);
            textoHtml = "<html><font face='Roboto' size='4'><b>" + data.getNombre() + "</b></font></html>";
            lbNombre.setText(textoHtml);
            lbNombre.setVisible(true);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        } else if (data.getTipo() == ModeloMenu.TipoMenu.VACIO || data.getNombre().trim().isEmpty()) {
            lbIcono.setVisible(false);
            lbNombre.setText(" ");
            lbNombre.setVisible(true);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        } else {
            if (data.isSubMenuChild()) {
                paddingLeft = 30;
            }
            setBorder(BorderFactory.createEmptyBorder(2, paddingLeft, 2, 5));
            lbIcono.setVisible(true);
            lbIcono.setIcon(data.toIcon());

            if (data.getTipo() == ModeloMenu.TipoMenu.SUB_MENU) {
                // la flecha tiene su propio tamaño de fuente "</font>  <font size='2'>&#9660;</font></html>".
                if (data.isExpandible()) {
                    textoHtml = "<html><font face='Roboto' size='3'>" + data.getNombre() + "</font>  <font size='2'>&#9660;</font></html>";
                } else {
                    textoHtml = "<html><font face='Roboto' size='3'>" + data.getNombre() + "</font>  <font size='2'>&#9654;</font></html>";
                }
            } else {
                textoHtml = "<html><font face='Roboto' size='3'>" + data.getNombre() + "</font></html>";
            }

            lbNombre.setText(textoHtml);
            lbNombre.setVisible(true);
        }
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcono = new javax.swing.JLabel();
        lbNombre = new javax.swing.JLabel();

        lbNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbNombre.setText("Nombre Menu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(lbNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbIcono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. DIBUJAR FONDO MÁS OSCURO para SUBMENÚS HIJOS
        if (data != null && data.isSubMenuChild()) {
            g2.setColor(SUBMENU_BACKGROUND);
            //dibuja el rectangulo oscuro
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // 2. DIBUJAR EFECTOS HOVER/SELECCIÓN
        if (seleccionado || isHover) {
            Color colorDeEfecto = new Color(255, 255, 255, 80);

            if (seleccionado) {
                colorDeEfecto = new Color(255, 255, 255, 120);
            }

            g2.setColor(colorDeEfecto);
            g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 5, 5);
        }

        super.paintComponent(g);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbIcono;
    private javax.swing.JLabel lbNombre;
    // End of variables declaration//GEN-END:variables

    public void setHover(boolean isHover) {
        this.isHover = isHover;
        repaint();
    }
}
