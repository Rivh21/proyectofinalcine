package com.ues.edu.controlador;

import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.dao.FuncionDAO;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalFunciones;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.time.format.DateTimeFormatter;

public class ControladorFunciones {

    private final Mantenimiento mantto;
    private final FuncionDAO daoFuncion;
    private final PeliculaDAO daoPelicula;
    private final SalaDAO daoSala;
    private Funcion funcionSelect;
    private ListaSimpleCircular<Funcion> listaFunciones;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ControladorFunciones(Mantenimiento mantto) {
        this.mantto = mantto;
        this.daoFuncion = new FuncionDAO();
        this.daoPelicula = new PeliculaDAO();
        this.daoSala = new SalaDAO();
        this.listaFunciones = new ListaSimpleCircular<>();

        cargarLista();
        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();

        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    public void cargarLista() {
        listaFunciones = daoFuncion.selectAll();
        funcionSelect = null;
        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);
        mostrar(listaFunciones);
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener(e -> {
            ModalFunciones mf = new ModalFunciones(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(mantto),
                    true,
                    "Agregar Función"
            );
            new ControladorModalFunciones(this, mf);
            mf.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener(e -> {
            if (funcionSelect == null) return;
            ModalFunciones mf = new ModalFunciones(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(mantto),
                    true,
                    "Editar Función"
            );
            new ControladorModalFunciones(this, mf, funcionSelect);
            mf.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener(e -> {
            if (funcionSelect == null) return;

            int op = javax.swing.JOptionPane.showConfirmDialog(
                    mantto,
                    "¿Seguro que quiere eliminar permanentemente la función?",
                    "Confirmar Eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (op == javax.swing.JOptionPane.YES_OPTION) {
                if (daoFuncion.delete(funcionSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Función eliminada", DesktopNotify.SUCCESS, 3000);
                    cargarLista(); 
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la función", DesktopNotify.ERROR, 3000);
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

                    funcionSelect = daoFuncion.buscarPorId(id);

                    boolean seleccionado = funcionSelect != null;
                    mantto.btnEditar.setEnabled(seleccionado);
                    mantto.btnEliminar.setEnabled(seleccionado);
                    mantto.btnAgregar.setEnabled(!seleccionado);
                }
            }
        });
    }

    public void mostrar(ListaSimpleCircular<Funcion> lista) {
        DefaultTableModel modeloTabla = new DefaultTableModel();
        String titulos[] = {"ID", "PELÍCULA", "SALA", "FECHA Y HORA", "PRECIO"};
        modeloTabla.setColumnIdentifiers(titulos);

        for (Object obj : lista.toArray()) {
            Funcion f = (Funcion) obj;
            Object[] fila = {
                    f.getId_funcion(),
                    f.getPeliculaTitulo(),
                    f.getSalaNombre(),
                    f.getFecha_hora_inicio().format(formatter),
                    f.getPrecio_boleto()
            };
            modeloTabla.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        mantto.tbDatos.setRowSorter(sorter);
        mantto.tbDatos.setModel(modeloTabla);

        int[] anchos = {30, 200, 150, 150, 50};
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void keyReleasedBuscar() {
        mantto.tfBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String texto = mantto.tfBuscar.getText().trim();
                ListaSimpleCircular<Funcion> listaFiltrada =
                        texto.isEmpty() ? daoFuncion.selectAll() : daoFuncion.buscar(texto);

                mostrar(listaFiltrada);
            }
        });
    }
}
