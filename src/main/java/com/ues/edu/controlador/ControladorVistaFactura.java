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
import java.awt.Frame;
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

    public ControladorVistaFactura(VistaFactura vista, Usuario usuario) {
        this.vista = vista;
        this.facturaDao = new FacturaConcesionDao();
        this.usuarioLoggeado = usuario;

        inicializarTabla();
        cargarTablaFacturas();

       
        onClickNuevaFactura();
        onClickVerFactura();
    }

    private void inicializarTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"ID", "Total", "Método Pago", "Empleado"});
        vista.tbDatos.setModel(modelo);
    }

   
    private void onClickNuevaFactura() {
        vista.btnNuevaFactura.addActionListener(e -> {
            Frame owner = JOptionPane.getFrameForComponent(vista);
            ModalFactCon modal = new ModalFactCon(owner, true, "Nueva Factura");

            int idEmpleadoLoggeado;
            try {
                idEmpleadoLoggeado = this.usuarioLoggeado.getEmpleado().getIdEmpleado();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(vista, "Error: No se pudo obtener la información de sesión del empleado.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
                return;
            }

        
            new ControladorModalFactCon(modal, this, idEmpleadoLoggeado);

         
            modal.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    cargarTablaFacturas();
                }
            });

            modal.setVisible(true);
        });
    }

    private void onClickVerFactura() {
        vista.btnVerFactura.addActionListener(e -> {
            int fila = vista.tbDatos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una factura primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int idFactura = Integer.parseInt(vista.tbDatos.getValueAt(fila, 0).toString());
                FacturaConcesion factura = facturaDao.buscarPorId(idFactura);

                if (factura == null) {
                    JOptionPane.showMessageDialog(vista, "No se encontró la factura con ID: " + idFactura, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Frame owner = JOptionPane.getFrameForComponent(vista);
                ModalFacturaInfo modal = new ModalFacturaInfo(owner, true, "Detalle Factura #" + idFactura);

                new ControladorModalFactura(modal, factura);

                modal.setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Error al obtener ID de la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al cargar el detalle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }



    public void cargarTablaFacturas() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();
        modelo.setRowCount(0);

        ListaSimple<FacturaConcesion> facturas = facturaDao.selectAll();

        for (int i = 0; i < facturas.toArray().size(); i++) {
            FacturaConcesion f = facturas.get(i);

            String metodoPagoNombre = f.getMetodoPago() != null ? f.getMetodoPago().getnombreMetodo() : "N/A";
            String empleadoNombre = f.getEmpleado() != null ? f.getEmpleado().getNombre() : "N/A";

            modelo.addRow(new Object[]{
                f.getIdFacturaConcesion(),
                String.format("%.2f", f.getMontoTotal()),
                metodoPagoNombre,
                empleadoNombre
            });
        }
    }
}
