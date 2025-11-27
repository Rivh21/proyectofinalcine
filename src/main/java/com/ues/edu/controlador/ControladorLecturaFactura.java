/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.FacturaTaquillaDao;
import com.ues.edu.modelo.dao.PermisoRolDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalFactura;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorLecturaFactura {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    FacturaTaquilla facturaSelect;
    FacturaTaquillaDao daoFactura;
    private ListaSimple<FacturaTaquilla> listaActualMostrada;

    private Usuario usuarioActual;
    private List<String> permisosUsuario;

    public ControladorLecturaFactura(Mantenimiento mantto, Usuario usuario) {
        this.mantto = mantto;
        this.usuarioActual = usuario;
        this.daoFactura = new FacturaTaquillaDao();
        this.listaActualMostrada = daoFactura.selectAll();
        cargarPermisos();
        this.mantto.btnAgregar.setText("DETALLES");
        this.mantto.btnAgregar.setEnabled(false);
        this.mantto.btnEditar.setText("ANULAR");
        if (tienePermiso("ANULAR_FACTURAS") || tienePermiso("ACCESO_TOTAL")) {
            this.mantto.btnEditar.setVisible(true);
        } else {
            this.mantto.btnEditar.setVisible(false);
        }
        this.mantto.btnEditar.setEnabled(false); // Deshabilitado hasta seleccionar
        this.mantto.btnEliminar.setVisible(false);
        configurarSeleccion();
        forzarBoton();
        onClickDetalles();
        onClickAnular();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
    }

    private void cargarPermisos() {
        if (usuarioActual != null && usuarioActual.getRol() != null) {
            PermisoRolDao prDao = new PermisoRolDao();
            this.permisosUsuario = prDao.obtenerNombresPermisosPorRol(usuarioActual.getRol().getIdRol());
        } else {
            this.permisosUsuario = new ArrayList<>();
        }
    }

    private boolean tienePermiso(String permiso) {
        if (usuarioActual != null && "ADMINISTRADOR".equalsIgnoreCase(usuarioActual.getRol().getNombreRol())) {
            return true;
        }
        return permisosUsuario.contains(permiso);
    }

    private void configurarSeleccion() {
        this.mantto.tbDatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowVista = mantto.tbDatos.getSelectedRow();

                if (rowVista != -1) {
                    int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                    String idFacturaTaquilla = (String) mantto.tbDatos.getModel().getValueAt(rowModelo, 0);

                    facturaSelect = null;
                    if (listaActualMostrada != null) {
                        for (Object obj : listaActualMostrada.toArray()) {
                            FacturaTaquilla fc = (FacturaTaquilla) obj;
                            if (fc.getIdFacturaTaquilla().equals(idFacturaTaquilla)) {
                                facturaSelect = fc;
                                break;
                            }
                        }
                    }

                    mantto.btnAgregar.setEnabled(true);
                    boolean tienePermiso = tienePermiso("ANULAR_FACTURAS") || tienePermiso("ACCESO_TOTAL");
                    boolean esValida = facturaSelect != null && !"ANULADA".equals(facturaSelect.getEstado());
                    mantto.btnEditar.setEnabled(tienePermiso && esValida);
                } else {
                    facturaSelect = null;
                    mantto.btnAgregar.setEnabled(false);
                    mantto.btnEditar.setEnabled(false);
                }
            }
        });
    }
    private void onClickDetalles() {
        this.mantto.btnAgregar.addActionListener(e -> {
            if (facturaSelect != null) {
                ModalFactura mf = new ModalFactura(new JFrame(), true, "Detalles Factura: " + this.facturaSelect.getIdFacturaTaquilla());
                ControladorDetalleFactura cdf = new ControladorDetalleFactura(mf, facturaSelect);
                mf.setVisible(true);
                this.mantto.tbDatos.clearSelection();
            }
        });
    }

    private void onClickAnular() {
        this.mantto.btnEditar.addActionListener(e -> {
            if (!tienePermiso("ANULAR_FACTURAS") && !tienePermiso("ACCESO_TOTAL")) {
                JOptionPane.showMessageDialog(mantto, "No tiene permisos para realizar esta acción.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (facturaSelect != null) {
                int confirm = JOptionPane.showConfirmDialog(mantto,
                        "¿Está seguro de ANULAR esta factura?\nEsta acción liberará los asientos y no se puede deshacer.",
                        "Confirmar Anulación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (daoFactura.anularFactura(facturaSelect.getIdFacturaTaquilla())) {
                        DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                        DesktopNotify.showDesktopMessage("Éxito", "Factura anulada correctamente.", DesktopNotify.SUCCESS, 3000L);
                        this.listaActualMostrada = daoFactura.selectAll();
                        mostrar(listaActualMostrada);
                        this.mantto.tbDatos.clearSelection();
                    } else {
                        DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                        DesktopNotify.showDesktopMessage("Error", "No se pudo anular la factura.", DesktopNotify.FAIL, 3000L);
                    }
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<FacturaTaquilla> lista;
                if (textoBusqueda.isEmpty()) {
                    lista = daoFactura.selectAll();
                } else {
                    lista = daoFactura.buscar(textoBusqueda);
                }
                mostrar(lista);
            }
        });
    }

    private void mostrar(ListaSimple<FacturaTaquilla> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"ID", "VENDEDOR", "MÉTODO", "TOTAL", "DESCUENTO", "ESTADO"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<FacturaTaquilla> facturas = lista.toArray();
        if (facturas != null) {
            for (FacturaTaquilla fc : facturas) {
                Object[] fila = {
                    fc.getIdFacturaTaquilla(),
                    fc.getEmpleado().getNombre() + " " + fc.getEmpleado().getApellido(),
                    fc.getMetodoPago().getnombreMetodo(),
                    String.format("$%.2f", fc.getMontoTotal()),
                    String.format("$%.2f", fc.getDescuentoAplicado()),
                    fc.getEstado()
                };
                modelo.addRow(fila);
            }
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);

        int[] anchosFijos = {80, 200, 80, 70, 70, 70};
        ajustarAnchoColumnas(anchosFijos);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void forzarBoton() {
        this.mantto.fondo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mantto.btnAgregar.setEnabled(false);
                mantto.btnEditar.setEnabled(false);
            }
        });
    }
}
