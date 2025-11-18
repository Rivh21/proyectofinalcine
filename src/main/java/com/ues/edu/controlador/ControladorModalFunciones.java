package com.ues.edu.controlador;

/**
 *
 * @author radon
 */

import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.FuncionDAO;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.ModalFunciones;
import com.ues.edu.vista.VistaListado;
import com.ues.edu.utilidades.Validaciones;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import java.time.LocalDateTime;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

public class ControladorModalFunciones {

    private final ControladorFunciones cf;
    private final ModalFunciones mf;
    private final FuncionDAO daoFuncion;
    private final PeliculaDAO daoPelicula;
    private final SalaDAO daoSala;
    private Funcion funcionSelect;
    private Pelicula peliculaSeleccionada;
    private final Validaciones validador = new Validaciones();

    public ControladorModalFunciones(ControladorFunciones cf, ModalFunciones mf) {
        this(cf, mf, null);
    }

    public ControladorModalFunciones(ControladorFunciones cf, ModalFunciones mf, Funcion funcionSelect) {
        this.cf = cf;
        this.mf = mf;
        this.daoFuncion = new FuncionDAO();
        this.daoPelicula = new PeliculaDAO();
        this.daoSala = new SalaDAO();
        this.funcionSelect = funcionSelect;

        cargarSalas();
        if (funcionSelect != null) cargarDatos();
        onClickGuardar();
        onClickAsignarPeliculas();
    }

    private void onClickAsignarPeliculas() {
        mf.btnPeliculas.addActionListener(e -> {
            VistaListado vl = new VistaListado(new JFrame(), true, "Lista de Películas");
            new ControladorListadoPeliculas(this, vl);
            vl.setVisible(true);
        });
    }

    public void cargarPelicula(Pelicula p) {
        this.peliculaSeleccionada = p;
        mf.lbPeliculas.setText("Película: " + p.getTitulo());
    }

    private void onClickGuardar() {
        mf.btnGuardar.addActionListener(e -> {
            if (!validarCampos()) return;

            if (peliculaSeleccionada == null && funcionSelect != null) {
                peliculaSeleccionada = daoPelicula.buscarPorId(funcionSelect.getIdPelicula());
            }

            if (peliculaSeleccionada == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Debe seleccionar una película", DesktopNotify.ERROR, 3000);
                return;
            }

            Sala sala = (Sala) mf.cmbSalas.getSelectedItem();
            LocalDateTime fecha = mf.fechaIni.getDateTimeStrict();

            if (!validarFechaNoPasada(fecha)) return;
            if (!validarConflictos(peliculaSeleccionada, sala, fecha)) return;

            if (funcionSelect == null) agregarFuncion(peliculaSeleccionada, sala, fecha);
            else actualizarFuncion(peliculaSeleccionada, sala, fecha);
        });
    }

    private void agregarFuncion(Pelicula pelicula, Sala sala, LocalDateTime fecha) {
        try {
            double precio = Double.parseDouble(mf.tfPrecio.getText().trim());
            Funcion funcion = new Funcion(pelicula.getIdPelicula(), sala.getIdSala(), fecha, precio);
            funcion.setPeliculaTitulo(pelicula.getTitulo());
            funcion.setSalaNombre(sala.getNombreSala());

            if (daoFuncion.insert(funcion)) {

                // ✔ NOTIFICACIÓN DE AGREGADO
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage(
                        "Función agregada",
                        "La función se registró correctamente",
                        DesktopNotify.SUCCESS, 3500
                );

                mf.dispose();
                cf.mostrar(daoFuncion.selectAll());

            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "No se pudo agregar la función", DesktopNotify.ERROR, 3000);
            }
        } catch (Exception ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Datos inválidos o incompletos", DesktopNotify.ERROR, 3000);
        }
    }

    private void actualizarFuncion(Pelicula pelicula, Sala sala, LocalDateTime fecha) {
        try {
            double precio = Double.parseDouble(mf.tfPrecio.getText().trim());
            funcionSelect.setIdPelicula(pelicula.getIdPelicula());
            funcionSelect.setIdSala(sala.getIdSala());
            funcionSelect.setFechaHoraInicio(fecha);
            funcionSelect.setPrecioBoleto(precio);
            funcionSelect.setPeliculaTitulo(pelicula.getTitulo());
            funcionSelect.setSalaNombre(sala.getNombreSala());

            if (daoFuncion.update(funcionSelect)) {

                // ✔ NOTIFICACIÓN DE ACTUALIZACIÓN
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage(
                        "Función actualizada",
                        "Los datos fueron modificados correctamente",
                        DesktopNotify.SUCCESS, 3500
                );

                mf.dispose();
                cf.mostrar(daoFuncion.selectAll());

            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "No se pudo actualizar la función", DesktopNotify.ERROR, 3000);
            }
        } catch (Exception ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Datos inválidos o incompletos", DesktopNotify.ERROR, 3000);
        }
    }

    private boolean validarCampos() {
        if (mf.fechaIni.getDateTimeStrict() == null) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Seleccione una fecha válida", DesktopNotify.ERROR, 3000);
            return false;
        }
        String precioTxt = mf.tfPrecio.getText().trim();
        if (precioTxt.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Ingrese el precio del boleto", DesktopNotify.ERROR, 3000);
            return false;
        }
        try {
            double precio = Double.parseDouble(precioTxt);
            if (precio <= 0) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "El precio debe ser mayor que 0", DesktopNotify.ERROR, 3000);
                return false;
            }
        } catch (NumberFormatException ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Precio inválido", DesktopNotify.ERROR, 3000);
            return false;
        }
        return true;
    }

    private boolean validarConflictos(Pelicula pelicula, Sala sala, LocalDateTime fecha) {
        ListaSimpleCircular<Funcion> funciones = daoFuncion.selectAll();

        for (Object obj : funciones.toArray()) {
            Funcion f = (Funcion) obj;
            if (funcionSelect != null && f.getIdFuncion() == funcionSelect.getIdFuncion()) continue;

            if (f.getIdSala() == sala.getIdSala() &&
                f.getFechaHoraInicio().isEqual(fecha) &&
                f.getIdPelicula() != pelicula.getIdPelicula()) {

                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage(
                        "Error",
                        "Ya hay otra película programada a esa hora en la misma sala",
                        DesktopNotify.ERROR, 3000
                );
                return false;
            }

            if (f.getIdPelicula() == pelicula.getIdPelicula() &&
                f.getFechaHoraInicio().isEqual(fecha) &&
                f.getIdSala() != sala.getIdSala()) {

                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage(
                        "Error",
                        "La misma película ya está programada en otra sala a esa hora",
                        DesktopNotify.ERROR, 3000
                );
                return false;
            }

            if (f.getIdPelicula() == pelicula.getIdPelicula() &&
                f.getIdSala() == sala.getIdSala() &&
                f.getFechaHoraInicio().isEqual(fecha)) {

                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage(
                        "Error",
                        "La película ya está programada en esta sala a esta hora",
                        DesktopNotify.ERROR, 3000
                );
                return false;
            }
        }

        return true;
    }

    private boolean validarFechaNoPasada(LocalDateTime fecha) {
        LocalDateTime ahora = LocalDateTime.now();
        if (fecha.toLocalDate().isBefore(ahora.toLocalDate())) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "No se pueden programar funciones en días anteriores", DesktopNotify.ERROR, 3000);
            return false;
        } else if (fecha.toLocalDate().isEqual(ahora.toLocalDate()) && fecha.isBefore(ahora)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "No se pueden programar funciones en horas pasadas del mismo día", DesktopNotify.ERROR, 3000);
            return false;
        }
        return true;
    }

    private void cargarSalas() {
        DefaultComboBoxModel<Sala> modelSalas = new DefaultComboBoxModel<>();
        ListaSimpleCircular<Sala> listaSalas = daoSala.selectAll();
        for (Object obj : listaSalas.toArray()) {
            modelSalas.addElement((Sala) obj);
        }
        mf.cmbSalas.setModel(modelSalas);
    }

    private void cargarDatos() {
        mf.tfPrecio.setText(String.valueOf(funcionSelect.getPrecioBoleto()));
        mf.fechaIni.setDateTimeStrict(funcionSelect.getFechaHoraInicio());

        for (int i = 0; i < mf.cmbSalas.getItemCount(); i++) {
            Sala s = mf.cmbSalas.getItemAt(i);
            if (s.getIdSala() == funcionSelect.getIdSala()) {
                mf.cmbSalas.setSelectedIndex(i);
                break;
            }
        }

        if (funcionSelect.getPeliculaTitulo() != null) {
            mf.lbPeliculas.setText("Película: " + funcionSelect.getPeliculaTitulo());
            peliculaSeleccionada = daoPelicula.buscarPorId(funcionSelect.getIdPelicula());
        }
    }
}
