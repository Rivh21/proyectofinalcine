package com.ues.edu.controlador;

import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.dao.ProductoDao;
import com.ues.edu.modelo.estructuras.Pila;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalProducto;
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

public class ControladorProducto {

    Mantenimiento mantto;
    DefaultTableModel modelo;
    Producto productoSelect;
    ProductoDao daoProducto;
    private Pila<Producto> pilaActualMostrada;

    public ControladorProducto(Mantenimiento mantto) {
        daoProducto = new ProductoDao();
        this.mantto = mantto;
        this.pilaActualMostrada = daoProducto.selectAll();

        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();

        mostrar(pilaActualMostrada);
        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    public ProductoDao getDaoProducto() {
        return daoProducto; // Getter para que ModalProducto use el mismo DAO
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener((e) -> {
            ModalProducto mp = new ModalProducto(new JFrame(), true, "Agregar Producto");
            new ControladorModalProducto(this, mp); // usa DAO compartido
            mp.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener((e) -> {
            ModalProducto mp = new ModalProducto(new JFrame(), true, "Editar Producto");
            new ControladorModalProducto(this, mp, productoSelect); // usa DAO compartido
            mp.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (productoSelect == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(
                    this.mantto,
                    "¿Seguro que desea eliminar el producto " + productoSelect.getNombre() + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {
                if (daoProducto.delete(productoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Producto eliminado.",
                            DesktopNotify.SUCCESS, 3000);
                    cargarLista();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se pudo eliminar el producto.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = mantto.tfBuscar.getText().trim();
                Pila<Producto> pila;

                if (texto.isEmpty()) {
                    pila = daoProducto.selectAll();
                } else {
                    Producto porId = null;
                    Producto porNombre = null;

                    if (texto.matches("\\d+")) {
                        porId = daoProducto.buscar(Integer.parseInt(texto));
                    }

                    porNombre = daoProducto.buscarNombre(texto);

                    pila = new Pila<>();
                    if (porId != null) pila.push(porId);
                    if (porNombre != null) pila.push(porNombre);
                }

                mostrar(pila);
            }
        });
    }

    public void mostrar(Pila<Producto> pila) {
        this.pilaActualMostrada = pila;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "NOMBRE", "PRECIO VENTA"};
        modelo.setColumnIdentifiers(titulos);

        ArrayList<Producto> lista = pila.toArray();
        for (Producto p : lista) {
            Object[] fila = {
                p.getIdProducto(),
                p.getNombre(),
                String.format("$%.2f", p.getPrecioVenta())
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);

        int[] anchos = {30, 250, 80};
        ajustarAnchoColumnas(anchos);
    }

    private void onClickTabla() {
        this.mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == mantto.tbDatos) {
                    int rowVista = mantto.tbDatos.getSelectedRow();
                    if (rowVista >= 0) {
                        int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                        Object v = mantto.tbDatos.getModel().getValueAt(rowModelo, 0);
                        int id = (Integer) v;
                        productoSelect = null;

                        for (Producto p : pilaActualMostrada.toArray()) {
                            if (p.getIdProducto() == id) {
                                productoSelect = p;
                                break;
                            }
                        }

                        boolean sel = (productoSelect != null);
                        mantto.btnEditar.setEnabled(sel);
                        mantto.btnEliminar.setEnabled(sel);
                        mantto.btnAgregar.setEnabled(!sel);
                    }
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

    public void cargarLista() {
        mostrar(daoProducto.selectAll());
    }
}
