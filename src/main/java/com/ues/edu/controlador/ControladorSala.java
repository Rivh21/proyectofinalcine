package com.ues.edu.controlador;

import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalSalas;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class ControladorSala {

    private final Mantenimiento mantto;
    private final SalaDAO daoSala;
    private Sala salaSelect;
    private ListaSimpleCircular<Sala> listaSalas;

    public ControladorSala(Mantenimiento mantto) {
        this.mantto = mantto;
        this.daoSala = new SalaDAO();
        listaSalas = new ListaSimpleCircular<>();

        cargarLista();
        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();

        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);
    }

    public void cargarLista() {
        listaSalas = daoSala.selectAll();
        salaSelect = null;
        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);
        mostrar(listaSalas);
    }

    private void mostrar(ListaSimpleCircular<Sala> lista) {
        DefaultTableModel modelo = new DefaultTableModel();
        String titulos[] = {"ID", "NOMBRE"};
        modelo.setColumnIdentifiers(titulos);

        for (Sala s : lista.toArray()) {
            Object[] fila = {
                s.getId_sala(),
                s.getNombre_sala()
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        mantto.tbDatos.setRowSorter(sorter);
        mantto.tbDatos.setModel(modelo);

        int[] anchos = {50, 200};
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener(e -> {
            ModalSalas ms = new ModalSalas(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(mantto),
                    true,
                    "Agregar Sala"
            );
            new ControladorModalSala(this, ms);
            ms.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener(e -> {
            if (salaSelect == null) return;

            ModalSalas ms = new ModalSalas(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(mantto),
                    true,
                    "Editar Sala"
            );
            new ControladorModalSala(this, ms, salaSelect);
            ms.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener(e -> {
            if (salaSelect == null) return;

            int op = javax.swing.JOptionPane.showConfirmDialog(
                    mantto,
                    "¿Seguro que quiere eliminar la sala?",
                    "Confirmar Eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (op == javax.swing.JOptionPane.YES_OPTION) {
                if (daoSala.delete(salaSelect)) { // <-- ahora pasa el objeto Sala completo
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Sala eliminada", DesktopNotify.SUCCESS, 3000);
                    cargarLista();
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la sala", DesktopNotify.ERROR, 3000);
                }
            }
        });
    }

    private void onClickTabla() {
        mantto.tbDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int rowVista = mantto.tbDatos.getSelectedRow();
                if (rowVista >= 0) {
                    int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                    int id = (Integer) mantto.tbDatos.getModel().getValueAt(rowModelo, 0);

                    salaSelect = daoSala.buscarPorId(id); // <-- usar buscarPorId

                    boolean seleccionado = salaSelect != null;
                    mantto.btnEditar.setEnabled(seleccionado);
                    mantto.btnEliminar.setEnabled(seleccionado);
                    mantto.btnAgregar.setEnabled(!seleccionado);
                }
            }
        });
    }
}
