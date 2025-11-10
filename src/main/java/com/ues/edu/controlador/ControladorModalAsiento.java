/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.AsientoDao;
import com.ues.edu.vista.ModalAsientos;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

/**
 *
 * @author jorge
 */
public class ControladorModalAsiento {

    private ModalAsientos vistaModal;
    private Sala salaAsignada;
    private AsientoDao asientoDao;

    public ControladorModalAsiento(ModalAsientos vista, Sala salaRecibida) {
        this.vistaModal = vista;
        this.salaAsignada = salaRecibida;
        this.asientoDao = new AsientoDao();

        vista.lbSalaSeleccionada.setText(salaRecibida.getNombreSala().toUpperCase());

        onClickGenerar();
        onClickSalir();
    }

    private void onClickGenerar() {
        this.vistaModal.btnGenerarAsientos.addActionListener((e) -> {

            String inputFila = vistaModal.tfFilaInicial.getText().trim().toUpperCase();
            int numFilas = (Integer) vistaModal.spNumFila.getValue();
            int asientosPorFila = (Integer) vistaModal.spAsientosXFila.getValue();
            if (salaAsignada == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.LightBlue);
                DesktopNotify.showDesktopMessage("Advertencia", "Debe seleccionar una sala.", DesktopNotify.WARNING, 3000);
                return;
            }

            if (inputFila.isEmpty() || inputFila.length() != 1 || !Character.isLetter(inputFila.charAt(0))) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Ingrese una sola letra válida para la Fila Inicial.", DesktopNotify.ERROR, 3000);
                return;
            }

            char filaInicio = inputFila.charAt(0);

            char limiteFila = 'F';
            char ultimaFilaGenerada = (char) (filaInicio + numFilas - 1);
            if (filaInicio < 'A' || filaInicio > limiteFila || ultimaFilaGenerada > limiteFila) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error de Rango", "El rango de filas permitido es de 'A' a 'F'.", DesktopNotify.ERROR, 3000);
                return;
            }
            if (numFilas <= 0 || asientosPorFila <= 0) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Los valores de Filas y Asientos deben ser mayores a cero.", DesktopNotify.ERROR, 3000);
                return;
            }
            if (asientosPorFila > 15) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "El número máximo de asientos por fila es 15.", DesktopNotify.ERROR, 3000);
                return;
            }

            boolean exito = asientoDao.generate(
                    this.salaAsignada.getIdSala(),
                    filaInicio,
                    numFilas,
                    asientosPorFila
            );

            if (exito) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("Éxito", "Asientos generados correctamente para: " + this.salaAsignada.getNombreSala(), DesktopNotify.SUCCESS, 3000);
                this.vistaModal.dispose();
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Hubo un error en la inserción masiva. Verifique la Clave Única (asiento ya existe).", DesktopNotify.ERROR, 3000);
            }
        });
    }

    private void onClickSalir() {
        this.vistaModal.btnCancelar.addActionListener((e)->{
        this.vistaModal.dispose();
        });
    }
}
