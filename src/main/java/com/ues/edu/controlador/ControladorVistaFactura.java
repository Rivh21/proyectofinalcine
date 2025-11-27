/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.vista.VistaFactura;
import com.ues.edu.vista.ModalFactCon;
import com.ues.edu.vista.ModalFacturaInfo;
import com.ues.edu.modelo.dao.FacturaConcesionDao;
import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.DetalleConcesion;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.dao.PermisoRolDao;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 * 
 * @author radon
 */
public class ControladorVistaFactura {

    private final VistaFactura vista;
    private final FacturaConcesionDao facturaDao;
    private final Usuario usuarioLoggeado;

    private List<String> permisosUsuario;

    public ControladorVistaFactura(VistaFactura vista, Usuario usuario) {
        this.vista = vista;
        this.facturaDao = new FacturaConcesionDao();
        this.usuarioLoggeado = usuario;

        cargarPermisos();
        inicializarTabla();
        cargarTablaFacturas();
        configurarSeguridadBotones();
        onBuscar();
        onClickNuevaFactura();
        onClickVerFactura();
        onClickAnularFactura();
    }

    private void cargarPermisos() {
        if (usuarioLoggeado != null && usuarioLoggeado.getRol() != null) {
            PermisoRolDao prDao = new PermisoRolDao();
            permisosUsuario = prDao.obtenerNombresPermisosPorRol(
                    usuarioLoggeado.getRol().getIdRol()
            );
        } else {
            permisosUsuario = new ArrayList<>();
        }
    }

    private boolean tienePermiso(String permiso) {
        if (usuarioLoggeado != null &&
            "ADMINISTRADOR".equalsIgnoreCase(usuarioLoggeado.getRol().getNombreRol())) {
            return true;
        }
        return permisosUsuario.contains(permiso);
    }

    private void configurarSeguridadBotones() {

        vista.btnAnular.setVisible(false);
        vista.tbDatos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                int row = vista.tbDatos.getSelectedRow();
                boolean hayFila = (row != -1);
                if (hayFila) {
                    boolean permiso = tienePermiso("ANULAR_FACTURAS") || tienePermiso("ACCESO_TOTAL");
                    vista.btnAnular.setVisible(permiso);
                    vista.btnAnular.setEnabled(permiso);
                } else {
                    vista.btnAnular.setVisible(false);
                    vista.btnAnular.setEnabled(false);
                }
            }

        });
    }
    private void inicializarTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        modelo.setColumnIdentifiers(new Object[]{
                "ID", "Total", "Método Pago", "Empleado"
        });

        vista.tbDatos.setModel(modelo);
    }

    public void cargarTablaFacturas() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();
        modelo.setRowCount(0);

        ListaSimple<FacturaConcesion> facturas = facturaDao.selectAll();

        for (FacturaConcesion f : facturas.toArray()) {

            modelo.addRow(new Object[]{
                f.getIdFacturaConcesion(),
                String.format("%.2f", f.getMontoTotal()),
                f.getMetodoPago() != null ? f.getMetodoPago().getnombreMetodo() : "N/A",
                f.getEmpleado() != null ? f.getEmpleado().getNombre() : "N/A"
            });
        }
    }

    private void onClickNuevaFactura() {
        vista.btnNuevaFactura.addActionListener(e -> {
            Frame owner = JOptionPane.getFrameForComponent(vista);
            ModalFactCon modal = new ModalFactCon(owner, true, "Nueva Factura");

            try {
                int idEmpleadoLoggeado = this.usuarioLoggeado.getEmpleado().getIdEmpleado();
                new ControladorModalFactCon(modal, this, idEmpleadoLoggeado);
                modal.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        cargarTablaFacturas();
                    }
                });
                modal.setVisible(true);

            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error: No se pudo obtener la información del empleado.",
                        "Error Crítico",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private void onClickVerFactura() {
        vista.btnVerFactura.addActionListener(e -> {

            int fila = vista.tbDatos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una factura primero.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String idFactura = vista.tbDatos.getValueAt(fila, 0).toString();
                FacturaConcesion factura = facturaDao.buscarPorId(idFactura);


                if (factura == null) {
                    JOptionPane.showMessageDialog(vista,
                            "No se encontró la factura con ID: " + idFactura,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Frame owner = JOptionPane.getFrameForComponent(vista);
                ModalFacturaInfo modal = new ModalFacturaInfo(owner, true,
                        "Detalle Factura #" + idFactura);

                new ControladorModalFactura(modal, factura);
                modal.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error al cargar el detalle: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void onClickAnularFactura() {
        vista.btnAnular.addActionListener(e -> {

            int fila = vista.tbDatos.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una factura primero.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!tienePermiso("ANULAR_FACTURAS") && !tienePermiso("ACCESO_TOTAL")) {
                JOptionPane.showMessageDialog(vista, "No tiene permisos para anular facturas.",
                        "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String idFactura = vista.tbDatos.getValueAt(fila, 0).toString();
         

            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de ANULAR esta factura?\nEsta acción no se puede deshacer.",
                    "Confirmar Anulación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                boolean exito = facturaDao.anularFacturaConcesion(idFactura);

                if (exito) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Éxito",
                            "Factura anulada correctamente.",
                            DesktopNotify.SUCCESS, 3000L);

                    cargarTablaFacturas();

                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error",
                            "No se pudo anular la factura.",
                            DesktopNotify.FAIL, 3000L);
                }
            }
        });
    }

    
    private void onBuscar() {
        vista.tfBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                ejecutarBusqueda();
            }
        });
    }
    private void ejecutarBusqueda() {
        String txt = vista.tfBuscar.getText().trim();
        DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();
        modelo.setRowCount(0);

        ListaSimple<FacturaConcesion> facturas;

        if (txt.isEmpty()) {
            facturas = facturaDao.selectAll();
        } else {
            facturas = facturaDao.buscar(txt);
        }

        for (FacturaConcesion f : facturas.toArray()) {
            modelo.addRow(new Object[]{
                f.getIdFacturaConcesion(),
                String.format("%.2f", f.getMontoTotal()),
                f.getMetodoPago() != null ? f.getMetodoPago().getnombreMetodo() : "N/A",
                f.getEmpleado() != null ? f.getEmpleado().getNombre() : "N/A"
            });
        }
    }


}

