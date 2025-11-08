/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import com.ues.edu.interfaces.MenuSelectionListener;
import com.ues.edu.modelo.ModeloMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class ListaMenu<E extends Object> extends JList<E> {

    private final DefaultListModel modelo;
    private int selectedIndex = -1;
    private int hoverIndex = -1;

    // VARIABLE PARA EL LISTENER DE SELECCIÓN DE MÓDULOS
    private MenuSelectionListener selectionListener;

    public ListaMenu() {
        modelo = new DefaultListModel();
        setModel(modelo);

        initMouseListeners();
    }

    private void initMouseListeners() {
        // Listener para la SELECCIÓN (al hacer clic) y Expansión de Submenús
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    if (index < 0 || index >= modelo.getSize()) {
                        return; // Evitar errores fuera de rango
                    }
                    Object o = modelo.getElementAt(index);

                    if (o instanceof ModeloMenu menu) {

                        if (null == menu.getTipo()) {
                            // Si es TÍTULO o VACÍO
                            selectedIndex = -1;
                        } else switch (menu.getTipo()) {
                            case MENU -> {
                                // 1. ÍTEM DE MENÚ NORMAL: Selecciona y notifica al Dashboard
                                selectedIndex = index;
                                if (selectionListener != null) {
                                    selectionListener.onModuleSelected(menu.getNombre());
                                }
                            }
                            case SUB_MENU -> {
                                // 2. ÍTEM DE SUBMENÚ PADRE: Alterna la expansión
                                if (menu.isExpandible()) {
                                    // Colapsar
                                    hideSubMenu(menu, index);
                                } else {
                                    // Expandir
                                    showSubMenu(menu, index);
                                }   menu.setExpandible(!menu.isExpandible());
                                selectedIndex = index; // Opcional: selecciona el padre al expandir
                            }
                            default -> // Si es TÍTULO o VACÍO
                                selectedIndex = -1;
                        }

                        repaint();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (hoverIndex != -1) {
                    hoverIndex = -1;
                    repaint();
                }
            }
        });

        // Listener de MOVIMIENTO (HOVER)
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                int index = locationToIndex(me.getPoint());
                int newHoverIndex = -1;

                if (index >= 0 && index < modelo.getSize()) {
                    Object o = modelo.getElementAt(index);

                    if (o instanceof ModeloMenu menu
                            && (menu.getTipo() == ModeloMenu.TipoMenu.MENU || menu.getTipo() == ModeloMenu.TipoMenu.SUB_MENU)) {
                        newHoverIndex = index;
                    }
                }

                if (newHoverIndex != hoverIndex) {
                    hoverIndex = newHoverIndex;
                    repaint();
                }
            }
        });
    }

    // --- LÓGICA DE MANEJO DE SUBMENÚS ---
    // Método para mostrar los submenús
    private void showSubMenu(ModeloMenu parentMenu, int index) {
        List<ModeloMenu> subMenus = parentMenu.getSubMenus();
        if (subMenus == null || subMenus.isEmpty()) {
            return;
        }

        int current = index;
        for (ModeloMenu subMenu : subMenus) {
            modelo.insertElementAt(subMenu, ++current);
        }
    }

    // Método para ocultar los submenús
    private void hideSubMenu(ModeloMenu parentMenu, int index) {
        List<ModeloMenu> subMenus = parentMenu.getSubMenus();
        if (subMenus == null || subMenus.isEmpty()) {
            return;
        }

        // Remueve tantos elementos como submenús haya.
        int count = subMenus.size();
        for (int i = 0; i < count; i++) {
            // Siempre remueve el elemento que sigue inmediatamente al padre
            modelo.remove(index + 1);
        }
    }

    // --- GETTERS Y SETTERS ---
    public void setSelectionListener(MenuSelectionListener listener) {
        this.selectionListener = listener;
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object o, int index, boolean selected, boolean focus) {

                ModeloMenu dato;
                if (o instanceof ModeloMenu modeloMenu) {
                    dato = modeloMenu;
                } else {
                    dato = new ModeloMenu("", o + "", ModeloMenu.TipoMenu.VACIO);
                }

                // MenuItem.java debe manejar el sangrado y el ícono de expansión
                MenuItem item = new MenuItem(dato);

                // 1. Configuración de la Selección
                item.setSeleccionado(selectedIndex == index);

                // 2. Configuración del Hover
                item.setHover(hoverIndex == index);

                return item;
            }
        };
    }

    public void addItem(ModeloMenu data) {
        modelo.addElement(data);
    }

    public void addItems(List<ModeloMenu> data) {
        for (ModeloMenu item : data) {
            modelo.addElement(item);
        }
    }
}
