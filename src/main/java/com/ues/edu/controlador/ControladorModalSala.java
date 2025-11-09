package com.ues.edu.controlador;

import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.vista.ModalSalas;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

/**
 * 
 * @author radon
 */
public class ControladorModalSala {

    private final ControladorSala controladorSala;
    private final ModalSalas ms;
    private final SalaDAO dao;
    private Sala salaSelect;

    public ControladorModalSala(ControladorSala controladorSala, ModalSalas ms) {
        this(controladorSala, ms, null);
    }

    public ControladorModalSala(ControladorSala controladorSala, ModalSalas ms, Sala salaSelect) {
        this.controladorSala = controladorSala;
        this.ms = ms;
        this.dao = new SalaDAO();
        this.salaSelect = salaSelect;
        if (salaSelect != null) cargarDatos();
        onClickGuardar();
    }

    private void cargarDatos() {
        ms.tfNombreSalas.setText(salaSelect.getNombreSala());
    }

    private void onClickGuardar() {
        ms.btnGuardar.addActionListener(e -> {
            String nombre = ms.tfNombreSalas.getText().trim();

            if (nombre.isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Debe ingresar el nombre de la sala", DesktopNotify.ERROR, 3000);
                return;
            }

            Sala existente = dao.buscarPorNombre(nombre);

            if (existente != null && (salaSelect == null || existente.getIdSala() != salaSelect.getIdSala())) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Ya existe una sala con ese nombre", DesktopNotify.ERROR, 3000);
                return;
            }

            boolean exito;
            if (salaSelect == null) {
                Sala nuevaSala = new Sala(0, nombre);
                exito = dao.insert(nuevaSala);
            } else {
                salaSelect.setNombreSala(nombre);
                exito = dao.update(salaSelect);
            }

            if (exito) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("OK", "Sala guardada correctamente", DesktopNotify.SUCCESS, 3000);
                controladorSala.cargarLista();
                ms.dispose();
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "No se pudo guardar la sala", DesktopNotify.ERROR, 3000);
            }
        });
    }
}
