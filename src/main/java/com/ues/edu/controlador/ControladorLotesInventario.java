package com.ues.edu.controlador;

import com.ues.edu.modelo.LotesInventario;
import com.ues.edu.modelo.dao.LotesInventarioDao;
import com.ues.edu.modelo.estructuras.PrioridadCola;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalLotesInventario;

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
import java.util.ArrayList;

public class ControladorLotesInventario {

    private final Mantenimiento mantto;
    private final LotesInventarioDao dao;
    private PrioridadCola<LotesInventario> colaActual;
    private LotesInventario loteSelect;
    private DefaultTableModel modelo;

    public ControladorLotesInventario(Mantenimiento mantto) {
        this.mantto = mantto;
        this.dao = new LotesInventarioDao();
        colaActual = construirCola();
        mostrar(colaActual);
        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);

        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
    }

    private PrioridadCola<LotesInventario> construirCola() {
        PrioridadCola<LotesInventario> cola = new PrioridadCola<>(3);

        for (Object o : dao.selectAll().toArray()) {
            LotesInventario l = (LotesInventario) o;
            l.calcularPrioridad(); 
            cola.offer(l, l.getPrioridad());
        }

        return cola;
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener(e -> {
            ModalLotesInventario modal = new ModalLotesInventario(new JFrame(), true, "Registrar Lote");
            new ControladorModalLotesInventario(this, modal);
            modal.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener(e -> {
            if (loteSelect == null) return;

            ModalLotesInventario modal = new ModalLotesInventario(new JFrame(), true, "Editar Lote");
            new ControladorModalLotesInventario(this, modal, loteSelect);
            modal.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener(e -> {
            if (loteSelect == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un lote para eliminar");
                return;
            }

            int op = JOptionPane.showConfirmDialog(
                    mantto,
                    "¿Seguro que desea eliminar el lote " + loteSelect.getIdLote() + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {
                if (dao.delete(loteSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Lote eliminado", DesktopNotify.SUCCESS, 3000);
                    refrescar();
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar el lote", DesktopNotify.ERROR, 3000);
                }
            }
        });
    }

   private void keyReleasedBuscar() {
    mantto.tfBuscar.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String texto = mantto.tfBuscar.getText().trim();
            PrioridadCola<LotesInventario> lista = new PrioridadCola<>(3);

            if (texto.isEmpty()) {
                lista = dao.selectAll();
            } else {
                LotesInventario l = texto.matches("\\d+") ? dao.buscar(Integer.parseInt(texto)) : null;
                if (l != null) lista.offer(l, l.getPrioridad());

                LotesInventario l2 = dao.buscarPorNombreProducto(texto);
                if (l2 != null && (l == null || l2.getIdLote() != l.getIdLote()))
                    lista.offer(l2, l2.getPrioridad());
            }

            mostrar(lista);
        }
    });
}


    public void mostrar(PrioridadCola<LotesInventario> cola) {
    this.colaActual = cola;

    modelo = new DefaultTableModel();
    String[] titulos = {"ID LOTE", "PRODUCTO", "CANTIDAD", "FECHA CADUCIDAD", "FECHA RECEPCIÓN"};
    modelo.setColumnIdentifiers(titulos);

    ArrayList<LotesInventario> lista = new ArrayList<>();
    for (Object o : cola.toArray()) {
        LotesInventario l = (LotesInventario) o;
        l.calcularPrioridad(); 
        lista.add(l);
    }

    lista.sort((l1, l2) -> {
        int cmp = Integer.compare(l1.getPrioridad(), l2.getPrioridad());
        if (cmp == 0) { // si la prioridad es igual, ordenar por fecha caducidad
            return l1.getFechaCaducidad().compareTo(l2.getFechaCaducidad());
        }
        return cmp;
    });

    for (LotesInventario l : lista) {
        modelo.addRow(new Object[]{
            l.getIdLote(),
            l.getProductoNombre(),
            l.getCantidadDisponible(),
            l.getFechaCaducidad(),
            l.getFechaRecepcion()
        });
    }

    mantto.tbDatos.setModel(modelo);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
    mantto.tbDatos.setRowSorter(sorter);
    ajustarColumnas(new int[]{60, 200, 80, 130, 130});
}

  


    private void ajustarColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < anchos.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void onClickTabla() {
        mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowVista = mantto.tbDatos.getSelectedRow();
                if (rowVista < 0) return;

                int filaModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                int id = (int) mantto.tbDatos.getModel().getValueAt(filaModelo, 0);

                loteSelect = null;
                for (Object o : colaActual.toArray()) {
                    LotesInventario l = (LotesInventario) o;
                    if (l.getIdLote() == id) {
                        loteSelect = l;
                        break;
                    }
                }

                boolean seleccionado = (loteSelect != null);
                mantto.btnEditar.setEnabled(seleccionado);
                mantto.btnEliminar.setEnabled(seleccionado);
                mantto.btnAgregar.setEnabled(!seleccionado);
            }
        });
    }

    public void refrescar() {
        mostrar(construirCola());
    }

    public void seleccionarLoteEnTabla(int idLote) {
        PrioridadCola<LotesInventario> nuevaCola = construirCola();
        mostrar(nuevaCola);

        for (int i = 0; i < mantto.tbDatos.getRowCount(); i++) {
            int id = (int) mantto.tbDatos.getValueAt(i, 0);
            if (id == idLote) {
                mantto.tbDatos.setRowSelectionInterval(i, i);

                for (Object o : nuevaCola.toArray()) {
                    LotesInventario l = (LotesInventario) o;
                    if (l.getIdLote() == id) {
                        loteSelect = l;
                        break;
                    }
                }

                mantto.btnEditar.setEnabled(true);
                mantto.btnEliminar.setEnabled(true);
                mantto.btnAgregar.setEnabled(false);
                break;
            }
        }
    }
}
