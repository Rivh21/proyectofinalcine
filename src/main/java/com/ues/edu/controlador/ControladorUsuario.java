/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.UsuarioDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalUsuario;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorUsuario {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    Usuario usuarioSelect;
    UsuarioDao daoUsuario;
    private ListaSimple<Usuario> listaActualMostrada;

    public ControladorUsuario(Mantenimiento mantto) {
        daoUsuario = new UsuarioDao();
        this.mantto = mantto;
        this.listaActualMostrada = daoUsuario.selectAll();
        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    private void onClickTabla() {
        this.mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == mantto.tbDatos) {
                    int rowVista = mantto.tbDatos.getSelectedRow();

                    if (rowVista >= 0) {
                        int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);

                        Object idValue = mantto.tbDatos.getModel().getValueAt(rowModelo, 0);
                        int idUsuario = (Integer) idValue;

                        usuarioSelect = null;
                        for (Usuario user : listaActualMostrada.toArray()) {
                            if (user.getIdUsuario() == idUsuario) {
                                usuarioSelect = user;
                                break;
                            }
                        }

                        boolean seleccionado = (usuarioSelect != null);
                        mantto.btnEditar.setEnabled(seleccionado);
                        mantto.btnEliminar.setEnabled(seleccionado);
                        mantto.btnAgregar.setEnabled(!seleccionado);
                    }
                }
            }
        });
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener((e) -> {
            ModalUsuario mu = new ModalUsuario(new JFrame(), true, "Agregar Usuario");
            ControladorModalUsuario cmu = new ControladorModalUsuario(this, mu);
            mu.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener((e) -> {
            ModalUsuario mu = new ModalUsuario(new JFrame(), true, "Editar Usuario");
            ControladorModalUsuario cmu = new ControladorModalUsuario(this, mu, usuarioSelect);
            mu.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (usuarioSelect == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado para eliminar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int op = JOptionPane.showConfirmDialog(
                    this.mantto,
                    "¿Seguro que quiere eliminar permanentemente a " + usuarioSelect.getNombreUsuario() + "?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {

                if (daoUsuario.delete(usuarioSelect)) {
                    // Éxito: Notificación y actualización de la vista
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Registro Eliminado de la Base de Datos",
                            DesktopNotify.SUCCESS, 3000);

                    mostrar(daoUsuario.selectAll());

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error: No se pudo eliminar el registro en la base de datos.",
                            "Error de Operación", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<Usuario> lista;

                if (textoBusqueda.isEmpty()) {
                    lista = daoUsuario.selectAll();
                } else {
                    lista = daoUsuario.buscar(textoBusqueda);
                }

                if (lista.isEmpty() && !textoBusqueda.isEmpty()) {
                    mostrar(new ListaSimple<>());
                } else {
                    mostrar(lista);
                }
            }
        });
    }

    public void mostrar(ListaSimple<Usuario> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"N", "NOMBRE USUARIO", "NOMBRE", "APELLIDO", "ROL"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<Usuario> listaUsuarios = lista.toArray();
        for (Usuario user : listaUsuarios) {
            Object[] fila = {
                user.getIdUsuario(),
                user.getNombreUsuario(),
                user.getEmpleado().getNombre(),
                user.getEmpleado().getApellido(),
                user.getRol().getNombreRol()
            };
            modelo.addRow(fila);
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);
        // Ajustar ancho de columnas
        int[] anchosFijos = {30, 50, 100, 100, 80};

        ajustarAnchoColumnas(anchosFijos);
        mantto.tbDatos.setModel(modelo);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}
