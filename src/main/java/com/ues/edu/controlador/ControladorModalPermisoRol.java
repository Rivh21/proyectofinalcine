package com.ues.edu.controlador;

import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.PermisoRol;
import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.dao.PermisoDao;
import com.ues.edu.modelo.dao.PermisoRolDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.ModalPermisoRol;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

/**
 * 
 * @author radonay
 */
public class ControladorModalPermisoRol {

    private final ModalPermisoRol vista;
    private final int idRol;
    private final String nombreRol;
    private final PermisoDao permisoDao;
    private final PermisoRolDao permisoRolDao;

    public ControladorModalPermisoRol(ModalPermisoRol vista, int idRol, String nombreRol) {
        this.vista = vista;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.permisoDao = new PermisoDao();
        this.permisoRolDao = new PermisoRolDao();

        inicializarVista();
        onClickGuardar();
    }

    private void inicializarVista() {
        vista.lbTitulo.setText("Asignar Permiso a Rol");
        vista.lbRolSeleccionada.setText(nombreRol.toUpperCase());
        cargarPermisos();
    }

    private void cargarPermisos() {
        try {
            ListaSimple<Permiso> lista = permisoDao.selectAll();
            vista.cmbPermisos.removeAllItems();
            var asignadosArray = permisoRolDao.selectByRol(idRol).toArray();
            if (lista != null && !lista.isEmpty()) {
                for (Permiso permiso : lista.toArray()) {
                    boolean yaAsignado = asignadosArray.stream()
                            .anyMatch(pr -> pr.getPermiso().getIdPermiso() == permiso.getIdPermiso());
                    if (!yaAsignado) {
                        vista.cmbPermisos.addItem(permiso.getNombrePermiso());
                    }
                }
            }
            if (vista.cmbPermisos.getItemCount() == 0) {
                vista.cmbPermisos.addItem("No hay permisos disponibles");
                vista.cmbPermisos.setEnabled(false);
                vista.btnGuardar.setEnabled(false);
            } else {
                vista.cmbPermisos.setEnabled(true);
                vista.btnGuardar.setEnabled(true);
            }
        } catch (Exception e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error",
                "Error al cargar permisos: " + e.getMessage(),
                DesktopNotify.ERROR, 3000);
        }
    }

    private void onClickGuardar() {
        vista.btnGuardar.addActionListener((e) -> guardarPermisoRol());
    }
    private void guardarPermisoRol() {
        String nombrePermiso = (String) vista.cmbPermisos.getSelectedItem();

        if (nombrePermiso == null || nombrePermiso.equals("No hay permisos disponibles")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.LightBlue);
            DesktopNotify.showDesktopMessage("Aviso",
                "Seleccione un permiso válido.",
                DesktopNotify.WARNING, 3000);
            return;
        }

        try {
            ListaSimple<Permiso> lista = permisoDao.obtenerIdPorNombre(nombrePermiso);

            if (lista == null || lista.isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error",
                    "No se encontró el permiso seleccionado.",
                    DesktopNotify.ERROR, 3000);
                return;
            }

            Permiso permiso = lista.get(0);

            boolean yaExiste = permisoRolDao.selectByRol(idRol).toArray().stream().anyMatch(pr -> pr.getPermiso().getIdPermiso() == permiso.getIdPermiso());

            if (yaExiste) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error",
                    "Este permiso ya está asignado a este rol.",
                    DesktopNotify.FAIL, 4000L);
                return;
            }
            PermisoRol pr = new PermisoRol(0, new Rol(idRol, nombreRol), permiso, true);
            boolean ok = permisoRolDao.insert(pr);

            if (ok) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("Éxito",
                    "Permiso asignado correctamente al rol.",
                    DesktopNotify.SUCCESS, 3000);
                vista.dispose();
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error",
                    "No se pudo asignar el permiso. Quizá ya está asignado.",
                    DesktopNotify.ERROR, 3000);
            }

        } catch (Exception ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error",
                "Error al guardar permiso: " + ex.getMessage(),
                DesktopNotify.ERROR, 3000);
        }
    }
}
