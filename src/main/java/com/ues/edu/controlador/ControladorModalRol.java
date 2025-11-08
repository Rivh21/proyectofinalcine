/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.dao.RolDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.ModalRol;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import javax.swing.JOptionPane;

/**
 *
 * @author jorge
 */
public class ControladorModalRol {

    ControladorRol cr;
    ModalRol mr;
    Rol rol;
    RolDao daoRol;
    Rol rolSelect;

    public ControladorModalRol(ControladorRol cr, ModalRol mr) {
        this.cr = cr;
        daoRol = new RolDao();
        this.mr = mr;
        rolSelect = null;
        onClickGuardar();
    }

    public ControladorModalRol(ControladorRol cr, ModalRol mr, Rol rolSelect) {
        this.cr = cr;
        daoRol = new RolDao();
        this.mr = mr;
        this.rolSelect = rolSelect;
        onClickGuardar();
        cargarDatos();
    }

    private void onClickGuardar() {
        this.mr.btnGuardar.addActionListener((e) -> {
            if (validarCampos()) {
                if (rolSelect == null) {
                    newRol();
                } else {
                    editRol();
                }
            }
        });
    }

    private boolean validarCampos() {
        String nombreRol = mr.tfNombreRol.getText().trim();
        if (nombreRol.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Nombre Rol es obligatorio", DesktopNotify.ERROR, 3000);
            mr.tfNombreRol.requestFocus();
            return false;
        }
        if (nombreRol.matches(".*\\d.*")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Nombre Rol no debe contener números.", DesktopNotify.ERROR, 3000);
            mr.tfNombreRol.requestFocus();
            return false;
        }

        String permiso = mr.tfPermiso.getText().trim();
        if (permiso.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Permisos es obligatorio", DesktopNotify.ERROR, 3000);
            mr.tfPermiso.requestFocus();
            return false;
        }
        
        if (permiso.matches(".*\\d.*")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Permisos no debe contener números.", DesktopNotify.ERROR, 3000);
            mr.tfPermiso.requestFocus();
            return false;
        }

        return true;
    }

    private void newRol() {
        this.rol = new Rol(mr.tfNombreRol.getText().toUpperCase(), mr.tfPermiso.getText().toUpperCase());
         if (!existeRol()) {
            if (daoRol.insert(rol)) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("Ok", "Registro creado", DesktopNotify.SUCCESS, 4000);
                this.mr.dispose();
                cr.mostrar(daoRol.selectAll());
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Error al guardor", DesktopNotify.ERROR, 9000);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Rol ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRol() {
        rolSelect.setNombreRol(mr.tfNombreRol.getText().toUpperCase());
        rolSelect.setPermisos(mr.tfPermiso.getText().toUpperCase());
        if (daoRol.update(rolSelect)) {
            this.mr.dispose();
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Ok", "Registro Actualizado", DesktopNotify.SUCCESS, 4000);
            cr.mostrar(daoRol.selectAll());
        }
    }

    private void cargarDatos() {
        mr.tfNombreRol.setText(rolSelect.getNombreRol());
        mr.tfPermiso.setText(rolSelect.getPermisos());
    }

    private boolean existeRol() {
        ListaSimple<Rol> lista = daoRol.selectAllTo("nombre_rol", rol.getNombreRol());
        return !lista.isEmpty();
    }

}
