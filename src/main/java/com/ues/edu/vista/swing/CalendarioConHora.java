/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

/**
 *
 * @author radon
 */


import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class CalendarioConHora extends DateTimePicker {
    
    public CalendarioConHora() {

        super(); // Llama al constructor de DateTimePicker
        inicializarPersonalizacion();

    }

    public CalendarioConHora(LocalDateTime initialDateTime) {
        super();
        this.setDateTimePermissive(initialDateTime);
        inicializarPersonalizacion();

    }

    private void inicializarPersonalizacion() {

//  Personalizar el DatePicker (Calendario)
                TimePickerSettings timeSettings = this.getTimePicker().getSettings();

        // Mostrar la hora en formato 24 horas (ej. 14:30)
        timeSettings.setFormatForDisplayTime("HH:mm");
        // Establecer un rango de horas, por ej. horario de oficina
        // timeSettings.setTimeRange(LocalTime.of(8, 0), LocalTime.of(18, 0));
        // Añadir un listener
        this.addDateTimeChangeListener(event -> {
            LocalDateTime nuevaFechaHora = event.getNewDateTimeStrict();
            if (nuevaFechaHora != null) {
                System.out.println("Nueva fecha y hora seleccionada: " + nuevaFechaHora);
            }

        });

    }

    public void seleccionarMananaAlMediodia() {
        LocalDateTime manana = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        LocalDateTime mananaAlMediodia = manana.with(LocalTime.of(12, 0));
        this.setDateTimeStrict(mananaAlMediodia);

    }

    public void limpiarSeleccion() {
        this.clear();
    }

    /**
     *
     * Método para obtener la fecha y hora como un Timestamp de SQL para
     * facilitar el guardado en la base de datos.
     * @return java.sql.Timestamp o null si está vacío.
     *
     */
    public java.sql.Timestamp getTimestamp() {
        LocalDateTime ldt = this.getDateTimeStrict();
        if (ldt != null) {

            return java.sql.Timestamp.valueOf(ldt);
        }
        return null;

    }
}