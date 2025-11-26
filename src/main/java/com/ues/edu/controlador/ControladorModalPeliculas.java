/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.modelo.ClasificacionPelicula;
import com.ues.edu.modelo.GeneroPelicula;
import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.utilidades.Validaciones;
import com.ues.edu.vista.ModalPeliculas;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

/**
 *
 * @author radon
 */
public class ControladorModalPeliculas {

    private final ControladorPeliculas ce;
    private final ModalPeliculas mp;
    private final PeliculaDAO daoPelicula;
    private Pelicula peliculaSelect;
    private final Validaciones validador = new Validaciones();

    public ControladorModalPeliculas(ControladorPeliculas ce, ModalPeliculas mp) {
        this.ce = ce;
        this.mp = mp;
        this.daoPelicula = new PeliculaDAO();
        this.peliculaSelect = null;
        onClickGuardar();
    }

    public ControladorModalPeliculas(ControladorPeliculas ce, ModalPeliculas mp, Pelicula peliculaSelect) {
        this.ce = ce;
        this.mp = mp;
        this.daoPelicula = new PeliculaDAO();
        this.peliculaSelect = peliculaSelect;
        cargarDatos();
        onClickGuardar();
    }

    private void onClickGuardar() {
        mp.btnGuardar.addActionListener(e -> {if (validarCampos()) {
                if (peliculaSelect == null) {
                    agregarPelicula();
                } else {
                    actualizarPelicula();
                }
            }
        });
    }

    private void agregarPelicula() {
        Pelicula pelicula = new Pelicula(
                mp.tfTitulo1.getText().trim(),
                Integer.parseInt(mp.tfDuracion.getText().trim()),
                (GeneroPelicula) mp.cmbGenero.getSelectedItem(),
                (ClasificacionPelicula) mp.cmbClasificacion.getSelectedItem()
        );

        if (!existePelicula()) {
            if (daoPelicula.insert(pelicula)) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("OK", "Película agregada", DesktopNotify.SUCCESS, 3000);
                mp.dispose();
                ce.mostrar(daoPelicula.selectAll());
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "No se pudo agregar la película", DesktopNotify.ERROR, 3000);
            }
        } else {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "La película ya existe", DesktopNotify.ERROR, 3000);
        }
    }

    private void actualizarPelicula() {
        peliculaSelect.setTitulo(mp.tfTitulo1.getText().trim());
        peliculaSelect.setDuracionMinutos(Integer.parseInt(mp.tfDuracion.getText().trim()));
        peliculaSelect.setGenero((GeneroPelicula) mp.cmbGenero.getSelectedItem());
        peliculaSelect.setClasificacion((ClasificacionPelicula) mp.cmbClasificacion.getSelectedItem());

        if (daoPelicula.update(peliculaSelect)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("OK", "Película actualizada", DesktopNotify.SUCCESS, 3000);
            mp.dispose();
            ce.mostrar(daoPelicula.selectAll());
        }
    }

    private boolean validarCampos() {

        String titulo = mp.tfTitulo1.getText().trim();
        if (titulo.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Título es obligatorio", DesktopNotify.ERROR, 3000);
            mp.tfTitulo1.requestFocus();
            return false;
        }
        String permitidos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789áéíóúÁÉÍÓÚñÑ ' -:,.!?&";
        for (char c : titulo.toCharArray()) {
            if (permitidos.indexOf(c) == -1) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage(
                        "Error",
                        "El Título contiene caracteres no válidos (solo letras, números, espacios y signos de puntuación comunes)",
                        DesktopNotify.ERROR,
                        4000
                );
                mp.tfTitulo1.requestFocus();
                return false;
            }
        }

        String duracionStr = mp.tfDuracion.getText().trim();
        if (duracionStr.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Duración es obligatorio", DesktopNotify.ERROR, 3000);
            mp.tfDuracion.requestFocus();
            return false;
        }
        try {
            int duracion = Integer.parseInt(duracionStr);
            if (duracion <= 0) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Duración debe ser mayor que 0", DesktopNotify.ERROR, 3000);
                mp.tfDuracion.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Duración debe ser un número válido", DesktopNotify.ERROR, 3000);
            mp.tfDuracion.requestFocus();
            return false;
        }

        return true;
    }

    private boolean existePelicula() {
        ListaSimpleCircular<Pelicula> lista = daoPelicula.buscar(mp.tfTitulo1.getText().trim());
        return !lista.isEmpty();
    }

    private void cargarDatos() {
        mp.tfTitulo1.setText(peliculaSelect.getTitulo());
        mp.tfDuracion.setText(String.valueOf(peliculaSelect.getDuracionMinutos()));
        mp.cmbGenero.setSelectedItem(peliculaSelect.getGenero());
        mp.cmbClasificacion.setSelectedItem(peliculaSelect.getClasificacion());
    }
}
