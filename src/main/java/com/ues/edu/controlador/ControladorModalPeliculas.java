package com.ues.edu.controlador;

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
        mp.btnGuardar.addActionListener(e -> {
            if (validarCampos()) {
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
                mp.tfGenero.getText().trim(),
                mp.cmbClasificacion.getSelectedItem().toString()
        );

        if (!existePelicula()) {
            if (daoPelicula.insert(pelicula)) {   // ← cambio
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("OK", "Película agregada", DesktopNotify.SUCCESS, 3000);
                mp.dispose();
                ce.mostrar(daoPelicula.selectAll()); // ← cambio
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
        peliculaSelect.setDuracion_minutos(Integer.parseInt(mp.tfDuracion.getText().trim()));
        peliculaSelect.setGenero(mp.tfGenero.getText().trim());
        peliculaSelect.setClasificacion(mp.cmbClasificacion.getSelectedItem().toString());

        if (daoPelicula.update(peliculaSelect)) { // ← cambio
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("OK", "Película actualizada", DesktopNotify.SUCCESS, 3000);
            mp.dispose();
            ce.mostrar(daoPelicula.selectAll()); // ← cambio
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
        if (!titulo.matches("[a-zA-Z0-9 ]+")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El Título solo puede contener letras y números", DesktopNotify.ERROR, 3000);
            mp.tfTitulo1.requestFocus();
            return false;
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


        String genero = mp.tfGenero.getText().trim();
        if (genero.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Género es obligatorio", DesktopNotify.ERROR, 3000);
            mp.tfGenero.requestFocus();
            return false;
        }
        if (!genero.matches("[a-zA-Z ]+")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El Género solo puede contener letras", DesktopNotify.ERROR, 3000);
            mp.tfGenero.requestFocus();
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
        mp.tfDuracion.setText(String.valueOf(peliculaSelect.getDuracion_minutos()));
        mp.tfGenero.setText(peliculaSelect.getGenero());
        mp.cmbClasificacion.setSelectedItem(peliculaSelect.getClasificacion());
    }
}
