/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author jorge
 */
public class ModeloMenu {

    private String icono;
    private String nombre;
    private TipoMenu tipo;
    private boolean expandible;
    private List<ModeloMenu> subMenus;
    private boolean subMenuChild;

    public static enum TipoMenu {
        TITULO, MENU, VACIO, SUB_MENU
    }

    public ModeloMenu() {
    }

    public ModeloMenu(String icono, String nombre, TipoMenu type) {
        this.icono = icono;
        this.nombre = nombre;
        this.tipo = type;
        this.subMenus = new ArrayList<>();
    }

    public ModeloMenu(String nombre, TipoMenu type) {
        this("", nombre, type);
    }

    public ModeloMenu(String icon, String nombre, List<ModeloMenu> subMenus) {
        this(icon, nombre, TipoMenu.SUB_MENU);
        this.subMenus = subMenus;
        // Marca a los hijos como subMenuChild
        for (ModeloMenu sub : subMenus) {
            sub.setSubMenuChild(true);
        }
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoMenu getTipo() {
        return tipo;
    }

    public void setTipo(TipoMenu tipo) {
        this.tipo = tipo;
    }

    public boolean isExpandible() {
        return expandible;
    }

    public void setExpandible(boolean expandible) {
        this.expandible = expandible;
    }

    public List<ModeloMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<ModeloMenu> subMenus) {
        this.subMenus = subMenus;
    }

    public boolean isSubMenuChild() {
        return subMenuChild;
    }

    public void setSubMenuChild(boolean subMenuChild) {
        this.subMenuChild = subMenuChild;
    }

    public Icon toIcon() {
        // 1. Verificar si hay un nombre de icono para buscar.
        if (this.icono == null || this.icono.trim().isEmpty()) {
            return null;
        }
        // 2. Construir la ruta usando la barra diagonal inicial '/' 
        String path = "/iconos/" + this.icono + ".png";

        // 3. Intentar obtener el recurso.
        java.net.URL location = getClass().getResource(path);

        // 4. Verificar si el recurso fue encontrado.
        if (location == null) {
            System.err.println("ERROR: Recurso de icono NO ENCONTRADO en la ruta: " + path);
            return new ImageIcon();
        }
        // 5. Si se encuentra, crea y devuelve el ImageIcon.
        return new ImageIcon(location);
    }
}
