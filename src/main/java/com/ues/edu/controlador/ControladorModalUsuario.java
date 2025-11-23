/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.RolDao;
import com.ues.edu.modelo.dao.UsuarioDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.utilidades.Encriptar;
import com.ues.edu.vista.ModalUsuario;
import com.ues.edu.vista.VistaListado;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jorge
 */
public class ControladorModalUsuario {

    ControladorUsuario cu;
    ModalUsuario mu;
    Usuario usuario;
    UsuarioDao daoUsuario;
    Usuario usuarioSelect;
    private Empleado empleadoAsociado;

    public ControladorModalUsuario(ControladorUsuario cu, ModalUsuario mu) {
        this.cu = cu;
        daoUsuario = new UsuarioDao();
        this.mu = mu;
        usuarioSelect = null;
        onClickGuardar();
        onClickAgregarEmp();
        llenarRol();
        this.mu.btnEmpleado.setEnabled(true);
    }

    public ControladorModalUsuario(ControladorUsuario cu, ModalUsuario mu, Usuario usuarioSelect) {
        this.cu = cu;
        daoUsuario = new UsuarioDao();
        this.mu = mu;
        this.usuarioSelect = daoUsuario.selectUsuarioParaEdicion(usuarioSelect.getIdUsuario());
        onClickGuardar();
        onClickAgregarEmp();
        llenarRol();
        cargarDatos();
    }

    private void onClickGuardar() {
        this.mu.btnGuardar.addActionListener((e) -> {
            if (validarCampos()) {
                if (usuarioSelect == null) {
                    newUsuario();
                } else {
                    editUsuario();
                }
            }
        });
    }

    private void onClickAgregarEmp() {
        this.mu.btnEmpleado.addActionListener((e) -> {
            VistaListado vl = new VistaListado(new JFrame(), true, "Lista de Empleados");
            ControladorListadoEmpleados cle = new ControladorListadoEmpleados(this, vl);
            vl.setVisible(true);
        });

    }

    private void llenarRol() {
        this.mu.cbRol.removeAllItems();
        RolDao daoRol = new RolDao();
        ListaSimple<Rol> listaRoles = daoRol.selectAll();
        Rol tituloCombo = new Rol(0, "---SELECCIONE UN ROL---");
        mu.cbRol.addItem(tituloCombo);
        for (Rol rol : listaRoles.toArray()) {
            mu.cbRol.addItem(rol);
        }
    }

    private void cargarDatos() {
        this.mu.tfUsuario.setText(this.usuarioSelect.getNombreUsuario());

        this.mu.cbRol.setSelectedItem(this.usuarioSelect.getRol());
        this.empleadoAsociado = this.usuarioSelect.getEmpleado();
        if (this.empleadoAsociado != null) {
            String empleado = this.empleadoAsociado.getNombre() + " " + this.empleadoAsociado.getApellido();
            this.mu.lbEmpleado.setText("EMPLEADO: " + empleado.toUpperCase());
        } else {
            this.mu.lbEmpleado.setText("EMPLEADO: SIN ASIGNAR");
        }
        this.mu.btnEmpleado.setEnabled(false);
    }

    private boolean validarCampos() {
        if (mu.tfUsuario.getText().trim().isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Usuario es obligatorio", DesktopNotify.ERROR, 3000);
            mu.tfUsuario.requestFocus();
            return false;
        }

        String password = new String(mu.pfPassword.getPassword());
        String confirmar = new String(mu.pfConfirmar.getPassword());

        if (usuarioSelect == null) {
            if (password.isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "El campo Contraseña es obligatorio", DesktopNotify.ERROR, 3000);
                mu.pfPassword.requestFocus();
                return false;
            }
        }

        if (!password.isEmpty()) {
            if (!password.equals(confirmar)) {
                String aviso = "*INGRESE LA MISMA CONTRASEÑA*";
                mu.lbAviso.setForeground(Color.red);
                mu.lbAviso.setText(aviso);
                mu.pfPassword.requestFocus();
                return false;
            }
        }

        mu.lbAviso.setText("");
        Rol rolSeleccionado = (Rol) this.mu.cbRol.getSelectedItem();

        if (rolSeleccionado != null && rolSeleccionado.getIdRol() == 0) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Debe seleccionar un Rol válido.", DesktopNotify.ERROR, 3000);
            this.mu.cbRol.requestFocus();
            return false;
        }

        if (this.empleadoAsociado == null) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Debe asociar un empleado al usuario.", DesktopNotify.ERROR, 3000);
            mu.btnEmpleado.requestFocus();
            return false;
        }

        return true;
    }

    private void newUsuario() {
        Rol rolSeleccionado = (Rol) this.mu.cbRol.getSelectedItem();
        String nombreUsuario = this.mu.tfUsuario.getText();
        String password = Encriptar.getStringMessageDigest(
                this.mu.pfPassword.getText(), Encriptar.SHA256);

        this.usuario = new Usuario(
                nombreUsuario,
                password,
                rolSeleccionado,
                this.empleadoAsociado
        );
        if (!daoUsuario.existeNombreUsuario(nombreUsuario)) {
            if (daoUsuario.insert(usuario)) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("Ok", "Registro Creado", DesktopNotify.SUCCESS, 4000);
                this.mu.dispose();
                cu.mostrar(daoUsuario.selectAll());
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Error al guardar", DesktopNotify.ERROR, 3000); // Se cambió SUCCESS a ERROR
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: El nombre de usuario '" + nombreUsuario + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editUsuario() {
        Rol rolSeleccionado = (Rol) this.mu.cbRol.getSelectedItem();
        String nuevaPassword = new String(this.mu.pfPassword.getPassword());

        if (!usuarioSelect.getNombreUsuario().equals(this.mu.tfUsuario.getText().trim())) {
            if (daoUsuario.existeNombreUsuario(this.mu.tfUsuario.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Error: El nuevo nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        usuarioSelect.setNombreUsuario(this.mu.tfUsuario.getText());
        usuarioSelect.setRol(rolSeleccionado);
        usuarioSelect.setEmpleado(this.empleadoAsociado);

        if (!nuevaPassword.isEmpty()) {
            String passwordEncriptada = Encriptar.getStringMessageDigest(
                    nuevaPassword, Encriptar.SHA256
            );
            usuarioSelect.setPassword(passwordEncriptada);
        }

        if (daoUsuario.update(usuarioSelect)) {
            this.mu.dispose();
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Ok", "Registro Actualizado", DesktopNotify.SUCCESS, 4000);
            cu.mostrar(daoUsuario.selectAll());
        } else {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al Actualizar", DesktopNotify.ERROR, 3000);
        }
    }

    public void cargarEmpleado(Empleado empleado) {
        this.empleadoAsociado = empleado;

        if (this.empleadoAsociado != null) {
            String emp = empleado.getNombre() + " " + empleado.getApellido();
            this.mu.lbEmpleado.setText("EMPLEADO: " + emp.toUpperCase());
        } else {
            this.mu.lbEmpleado.setText("Ningún empleado asociado");
        }
    }

}
