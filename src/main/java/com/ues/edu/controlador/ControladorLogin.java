/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.UsuarioDao;
import com.ues.edu.utilidades.Encriptar;
import com.ues.edu.vista.Dashboard;
import com.ues.edu.vista.Login;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

/**
 *
 * @author jorge
 */
public class ControladorLogin {

    Login vista;
    UsuarioDao daoUsuario;
    Usuario usuario;

    public ControladorLogin(Login vista) {
        this.vista = vista;
        this.daoUsuario = new UsuarioDao();
        iniciar();
    }

    private void iniciar() {
        this.vista.btnIngresar.addActionListener((e) -> {
            autenticar();
        });
    }

     private void autenticar() {
        String usuario = vista.tfUsuario.getText().trim();
        String clave = new String(vista.tfClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            vista.lbAdvertencia.setText("Debe llenar todos los campos");
            vista.lbAdvertencia.setVisible(true);
            return;
        }

        String claveEncriptada = Encriptar.getStringMessageDigest(clave, Encriptar.SHA256);
        Usuario usuarioLogueado = daoUsuario.login(usuario, claveEncriptada);

        if (usuarioLogueado != null) {

            vista.dispose();
            Dashboard dash = new Dashboard(usuarioLogueado);
            dash.setVisible(true);
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Credenciales correctas", "Bienvenido/a", DesktopNotify.SUCCESS, 4000);
        } else {
            vista.tfUsuario.setText("");
            vista.tfClave.setText("");
            vista.lbAdvertencia.setText("Usuario o contraseña incorrectos");
            vista.lbAdvertencia.setVisible(true);
        }

    }

}
