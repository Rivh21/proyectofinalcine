package com.ues.edu.controlador;

import com.ues.edu.modelo.dao.PermisoDao;
import com.ues.edu.modelo.dao.PermisoRolDao;
import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.PermisoRol;
import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.ModalPermisoRol;
import com.ues.edu.vista.VistaPermisoRol;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * 
 * @author radonay
 */
public class ControladorPermisoRol {

    private final PermisoRolDao prDao;
    private final PermisoDao pDao;
    private int idRolActual;
    private String nombreRolActual;
    private VistaPermisoRol vistaActual;
    private ListaSimple<PermisoRol> listaPermisoRolActual;

    public ControladorPermisoRol() {
        this.prDao = new PermisoRolDao();
        this.pDao = new PermisoDao();
    }

    public void cargarPermisosEnTabla(int idRol, String nombreRol, VistaPermisoRol vista) {
        this.idRolActual = idRol;
        this.nombreRolActual = nombreRol;
        this.vistaActual = vista;
        vista.lblTitulo.setText("Permisos del Rol: " + nombreRol);
        vista.btnCambios.addActionListener(e -> guardarCambiosPermisos(vista.tbDatos));
        vista.btnAsignar.addActionListener(e -> mostrarModalAsignarPermiso());
        recargarTabla();
    }

    public void recargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID_P", "Permiso", "Asignado", "ID_PR"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 2) ? Boolean.class : super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        JTable tabla = vistaActual.tbDatos;
        tabla.setModel(modelo);

        this.listaPermisoRolActual = prDao.selectByRol(idRolActual);

        for (PermisoRol pr : listaPermisoRolActual.toArray()) {
            modelo.addRow(new Object[]{
                pr.getPermiso().getIdPermiso(),
                pr.getPermiso().getNombrePermiso(),
                pr.getTienePermiso(),
                pr.getIdPermisoRol()
            });
        }

        TableColumnModel tcm = tabla.getColumnModel();
        if (tcm.getColumnCount() > 0) {
            for (int col : new int[]{0, 3}) {
                tcm.getColumn(col).setMinWidth(0);
                tcm.getColumn(col).setMaxWidth(0);
                tcm.getColumn(col).setPreferredWidth(0);
            }
            tcm.getColumn(2).setPreferredWidth(80);
        }
    }

    private void mostrarModalAsignarPermiso() {
        ModalPermisoRol modal = new ModalPermisoRol(new JFrame(), true);
        modal.setTitle("Asignar Permiso a Rol");
        modal.lbRolSeleccionada.setText(nombreRolActual);
        cargarPermisosDisponibles(modal.cmbPermisos);
        modal.btnGuardar.addActionListener(e -> guardarNuevoPermiso(modal));
        modal.setLocationRelativeTo(vistaActual);
        modal.setVisible(true);
    }

    private void cargarPermisosDisponibles(JComboBox<String> cmbPermisos) {
        cmbPermisos.removeAllItems();

        // Todos los permisos
        ListaSimple<Permiso> listaPermisos = pDao.selectAllPermisos();
        var listaArray = listaPermisos.toArray();

        // parte para permisos ya asignado
        var asignadosArray = prDao.selectByRol(idRolActual).toArray();

        for (Permiso permiso : listaArray) {
            boolean yaAsignado = asignadosArray.stream()
                    .anyMatch(pr -> pr.getPermiso().getIdPermiso() == permiso.getIdPermiso());
            if (!yaAsignado) {
                cmbPermisos.addItem(permiso.getNombrePermiso());
            }
        }

        if (cmbPermisos.getItemCount() == 0) {
            cmbPermisos.addItem("No hay permisos disponibles");
            cmbPermisos.setEnabled(false);
        } else {
            cmbPermisos.setEnabled(true);
        }
    }

    private void guardarNuevoPermiso(ModalPermisoRol modal) {
        String nombrePermiso = (String) modal.cmbPermisos.getSelectedItem();
        if (nombrePermiso == null || nombrePermiso.equals("No hay permisos disponibles")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Seleccione un permiso válido.", DesktopNotify.FAIL, 4000L);
            return;
        }
        var lista = pDao.obtenerIdPorNombre(nombrePermiso);
        if (lista.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "No se encontró el permiso.", DesktopNotify.FAIL, 4000L);
            return;
        }
        Permiso permiso = lista.get(0);
        boolean yaExiste = prDao.selectByRol(idRolActual).toArray().stream()
                .anyMatch(pr -> pr.getPermiso().getIdPermiso() == permiso.getIdPermiso());

        if (yaExiste) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Este permiso ya está asignado a este rol.", DesktopNotify.FAIL, 4000L);
            return;
        }

        PermisoRol nuevoPR = new PermisoRol(0, new Rol(idRolActual, nombreRolActual), permiso, true);
        if (prDao.insert(nuevoPR)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Éxito", "Permiso asignado correctamente al rol.", DesktopNotify.SUCCESS, 4000L);
            modal.dispose();
            recargarTabla();
        } else {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "No se pudo asignar el permiso.", DesktopNotify.FAIL, 4000L);
        }
    }

    private void guardarCambiosPermisos(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        int filasGuardadas = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int idPermiso = (int) modelo.getValueAt(i, 0);
            boolean tienePermiso = (boolean) modelo.getValueAt(i, 2);
            if (prDao.actualizarEstadoPermiso(idRolActual, idPermiso, tienePermiso)) {
                filasGuardadas++;
            }
        }

        if (filasGuardadas > 0) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Cambios guardados", "Se guardaron " + filasGuardadas + " cambios correctamente.", DesktopNotify.SUCCESS, 4000L);
        } else {
            DesktopNotify.setDefaultTheme(NotifyTheme.LightBlue);
            DesktopNotify.showDesktopMessage("Sin cambios", "No se detectaron modificaciones que guardar.", DesktopNotify.INFORMATION, 4000L);
        }

        Window window = SwingUtilities.getWindowAncestor(vistaActual);
        if (window != null) {
            window.dispose();
        }
    }
}
