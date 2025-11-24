/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ues.edu.vista.componentes;

import com.ues.edu.modelo.ModeloMenu;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.PermisoRolDao;
import com.ues.edu.vista.swing.ListaMenu;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author jorge
 */
public class Menu extends javax.swing.JPanel {

    private Usuario usuarioActual;
    private List<String> permisosDelUsuario;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        setOpaque(false);
        listaMenu1.setOpaque(false);
        lbTitulo.setForeground(Color.WHITE);
        this.permisosDelUsuario = new ArrayList<>();
    }

    public void initMenu(Usuario usuario) {
        this.usuarioActual = usuario;

        if (usuario != null && usuario.getRol() != null) {
            PermisoRolDao dao = new PermisoRolDao();
            this.permisosDelUsuario = dao.obtenerNombresPermisosPorRol(usuario.getRol().getIdRol());
        }

        construirMenu();
    }

    private boolean tienePermiso(String nombrePermiso) {
        if (usuarioActual != null && "ADMINISTRADOR".equalsIgnoreCase(usuarioActual.getRol().getNombreRol())) {
            return true;
        }
        return permisosDelUsuario.contains(nombrePermiso);
    }

    private void construirMenu() {
        listaMenu1.addItem(new ModeloMenu("home_24", "INICIO", ModeloMenu.TipoMenu.MENU));

        // --- 1. CARTELERA (Antes preguntabas por Peliculas, Salas, etc. Ahora solo por el GRUPO) ---
        if (tienePermiso("GESTIONAR_CARTELERA")) {
            List<ModeloMenu> subMenusNucleo = new ArrayList<>();
            subMenusNucleo.add(new ModeloMenu("movie", "Peliculas", ModeloMenu.TipoMenu.MENU));
            subMenusNucleo.add(new ModeloMenu("sala", "Salas", ModeloMenu.TipoMenu.MENU));
            subMenusNucleo.add(new ModeloMenu("asiento", "Asientos", ModeloMenu.TipoMenu.MENU));
            subMenusNucleo.add(new ModeloMenu("funcion", "Funciones", ModeloMenu.TipoMenu.MENU));

            listaMenu1.addItem(new ModeloMenu("theaters", "Nucleo Del Cine", subMenusNucleo));
        }

        // --- 2. RRHH (Usuarios, Roles, Empleados) ---
        if (tienePermiso("GESTIONAR_RRHH")) {
            List<ModeloMenu> subMenusGestion = new ArrayList<>();
            subMenusGestion.add(new ModeloMenu("grupo", "Empleados", ModeloMenu.TipoMenu.MENU));
            subMenusGestion.add(new ModeloMenu("user", "Usuarios", ModeloMenu.TipoMenu.MENU));
            subMenusGestion.add(new ModeloMenu("asignar_rol", "Roles", ModeloMenu.TipoMenu.MENU));
            subMenusGestion.add(new ModeloMenu("permiso", "Permisos", ModeloMenu.TipoMenu.MENU));

            listaMenu1.addItem(new ModeloMenu("settings", "Gestion Interna", subMenusGestion));
        }

        // --- 3. PRODUCTOS (Nuevo permiso sugerido) ---
        if (tienePermiso("GESTIONAR_INVENTARIO")) {
            List<ModeloMenu> subMenusConcesion = new ArrayList<>();
            subMenusConcesion.add(new ModeloMenu("hand_package", "Producto", ModeloMenu.TipoMenu.MENU));
            subMenusConcesion.add(new ModeloMenu("inventario", "Lote Inventario", ModeloMenu.TipoMenu.MENU));

            listaMenu1.addItem(new ModeloMenu("storefront", "Productos", subMenusConcesion));
        }

        // --- 4. VENTAS ---
        if (tienePermiso("VER_VENTAS")) {
            List<ModeloMenu> subMenuVentas = new ArrayList<>();

            subMenuVentas.add(new ModeloMenu("factura", "Factura Taquilla", ModeloMenu.TipoMenu.MENU));

            // Si quieres separar confitería, usa GESTIONAR_INVENTARIO o crea VER_CONFITERIA
            if (tienePermiso("GESTIONAR_INVENTARIO") || tienePermiso("VER_VENTAS")) {
                subMenuVentas.add(new ModeloMenu("factura", "Factura Concesion", ModeloMenu.TipoMenu.MENU));
            }

            subMenuVentas.add(new ModeloMenu("ticket", "Boletos", ModeloMenu.TipoMenu.MENU));

            // Métodos de pago solo para Admins o Gerentes
            if (tienePermiso("GESTIONAR_SISTEMA")) {
                subMenuVentas.add(new ModeloMenu("payments", "Metodo de Pago", ModeloMenu.TipoMenu.MENU));
            }

            listaMenu1.addItem(new ModeloMenu("sell", "Ventas", subMenuVentas));
        }

        listaMenu1.addItem(new ModeloMenu(" ", " ", ModeloMenu.TipoMenu.VACIO));
        listaMenu1.revalidate();
        listaMenu1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        lbTitulo = new javax.swing.JLabel();
        listaMenu1 = new com.ues.edu.vista.swing.ListaMenu<>();

        setPreferredSize(new java.awt.Dimension(215, 400));

        panelMoving.setOpaque(false);

        lbTitulo.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        lbTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lbTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tituloCine.png"))); // NOI18N
        lbTitulo.setText("Nexus Cinema");

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
            .addComponent(listaMenu1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                .addComponent(lbTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listaMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public ListaMenu<String> getListaMenu() {
        return listaMenu1;
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Visions of Grandeur 
//        GradientPaint gf = new GradientPaint(0, 0, Color.decode("#000046"), 0, getHeight(), Color.decode("#1cb5e0"));
        //Royal 
//        GradientPaint gf = new GradientPaint(0, 0, Color.decode("#141E30"), 0, getHeight(), Color.decode("#243B55"));
        //Deep Space
//        GradientPaint gf = new GradientPaint(0, 0, Color.decode("#000000"), 0, getHeight(), Color.decode("#434343"));
        //Mirage    
        GradientPaint gf = new GradientPaint(0, 0, Color.decode("#16222A"), 0, getHeight(), Color.decode("#3A6073"));
        g2.setPaint(gf);

        //sirve para redondear
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); 
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintChildren(g);
    }

    private int x;
    private int y;

    public void initMoving(JFrame frm) {
        panelMoving.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }
        });

        panelMoving.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent me) {
                frm.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbTitulo;
    private com.ues.edu.vista.swing.ListaMenu<String> listaMenu1;
    private javax.swing.JPanel panelMoving;
    // End of variables declaration//GEN-END:variables
}
