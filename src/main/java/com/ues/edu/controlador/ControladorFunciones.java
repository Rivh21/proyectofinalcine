/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.dao.FuncionDAO;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.utilidades.CustomDateFormatter;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalFunciones;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
/**
 * 
 * @author radon
 */
public class ControladorFunciones {

    private final Mantenimiento mantto;
    private final FuncionDAO daoFuncion;
    private final PeliculaDAO daoPelicula;
    private final SalaDAO daoSala;

    private Funcion funcionSelect;
    private ListaSimpleCircular<Funcion> listaActualMostrada;
    private DefaultTableModel modelo;

    public ControladorFunciones(Mantenimiento mantto) {
        this.mantto = mantto;
        this.daoFuncion = new FuncionDAO();
        this.daoPelicula = new PeliculaDAO();
        this.daoSala = new SalaDAO();

        this.listaActualMostrada = daoFuncion.selectAll();

        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);

        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener((e) -> {
            ModalFunciones mf = new ModalFunciones(new JFrame(), true, "Agregar Función");
            new ControladorModalFunciones(this, mf);
            mf.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener((e) -> {
            if (funcionSelect == null) {
                JOptionPane.showMessageDialog(null,
                        "Seleccione una función para editar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ModalFunciones mf = new ModalFunciones(new JFrame(), true, "Editar Función");
            new ControladorModalFunciones(this, mf, funcionSelect);
            mf.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (funcionSelect == null) {
                JOptionPane.showMessageDialog(null,
                        "Seleccione una función para eliminar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(
                    this.mantto,
                    "¿Seguro que quiere eliminar permanentemente la función?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {

                if (daoFuncion.delete(funcionSelect)) {

                    // ✔ Notificación original (NO SE REMOVE)
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage(
                            "OK",
                            "Función eliminada",
                            DesktopNotify.SUCCESS,
                            3000
                    );

                    mostrar(daoFuncion.selectAll());

                } else {

                    // ✔ Notificación original (NO SE REMOVE)
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage(
                            "Error",
                            "No se pudo eliminar la función",
                            DesktopNotify.ERROR,
                            3000
                    );
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = mantto.tfBuscar.getText().trim();

                ListaSimpleCircular<Funcion> lista;
                if (texto.isEmpty()) {
                    lista = daoFuncion.selectAll();
                } else {
                    lista = daoFuncion.buscar(texto);
                }

                mostrar(lista);
            }
        });
    }

    public void mostrar(ListaSimpleCircular<Funcion> lista) {
        this.listaActualMostrada = lista;

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"ID", "PELÍCULA", "SALA", "FECHA Y HORA", "PRECIO"};
        modelo.setColumnIdentifiers(titulos);

        for (Funcion f : lista.toArray()) {
            Object[] fila = {
                f.getIdFuncion(),
                f.getPeliculaTitulo(),
                f.getSalaNombre(),
                CustomDateFormatter.format(f.getFechaHoraInicio()),
                String.format("$%.2f", f.getPrecioBoleto())
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        mantto.tbDatos.setRowSorter(sorter);
        mantto.tbDatos.setModel(modelo);

        int[] anchos = {30, 200, 150, 180, 50};
        ajustarAnchoColumnas(anchos);
    }

    private void onClickTabla() {
        mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int rowVista = mantto.tbDatos.getSelectedRow();

                if (rowVista >= 0) {
                    int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                    int id = (Integer) mantto.tbDatos.getModel().getValueAt(rowModelo, 0);

                    funcionSelect = null;
                    for (Funcion f : listaActualMostrada.toArray()) {
                        if (f.getIdFuncion() == id) {
                            funcionSelect = f;
                            break;
                        }
                    }

                    boolean seleccionado = (funcionSelect != null);

                    mantto.btnEditar.setEnabled(seleccionado);
                    mantto.btnEliminar.setEnabled(seleccionado);
                    mantto.btnAgregar.setEnabled(!seleccionado);
                }
            }
        });
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}
