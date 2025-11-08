/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author jorge
 */
public class RelojLabel extends JLabel {

    public RelojLabel() {
        setFont(new Font("Roboto", Font.PLAIN, 18));
        setHorizontalAlignment(SwingConstants.CENTER);

        // El Timer se encarga de llamar al método que actualiza la hora
        Timer timer = new Timer(1000, e -> actualizarHora());
        timer.start();

        // Muestro la hora una primera vez para que no aparezca vacío
        actualizarHora();
    }

    private void actualizarHora() {
        Locale locale = new Locale("es", "SV");
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy, hh:mm:ss a", locale);
        String tiempoFormateado = ahora.format(formatter);

        // Actualizo el texto de ESTA MISMA etiqueta
        setText(tiempoFormateado.toUpperCase());
    }
}
