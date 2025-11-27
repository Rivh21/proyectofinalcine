/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

/**
 *
 * @author radon
 */
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author jorge
 */
public class DateChooser extends JDateChooser {

    private JTextField textField;

    public DateChooser() {
        super();
        inicializarPersonalizacion();
    }

    public DateChooser(Date date) {
        super(date);
        inicializarPersonalizacion();
    }

    private void inicializarPersonalizacion() {
        this.setDateFormatString("dd-MM-yy");
        this.getJCalendar().setWeekOfYearVisible(false);

        Component editor = this.getDateEditor().getUiComponent();
        if (editor instanceof JTextField jTextField) {
            this.textField = jTextField;
        }

        this.addPropertyChangeListener("date", (PropertyChangeEvent evt) -> {
            SwingUtilities.invokeLater(() -> {
                forzarColorDelTema();
            });
            Date nuevaFecha = getDate();
            if (nuevaFecha != null) {
                System.out.println("Fecha seleccionada en el JDateChooser: " + nuevaFecha);
            }
        });
        UIManager.addPropertyChangeListener(evt -> {
            if ("lookAndFeel".equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(() -> {
                    forzarColorDelTema();
                });
            }
        });
        SwingUtilities.invokeLater(() -> {
            forzarColorDelTema();
        });
    }

    private void forzarColorDelTema() {
        if (this.textField != null) {
            Color colorDelTema = UIManager.getColor("TextField.foreground");
            if (colorDelTema != null) {
                this.textField.setForeground(colorDelTema);
            }
        }
    }
    
    public void seleccionarDiaDeMañana() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1); 
        this.setDate(cal.getTime());
    }

    public String getFechaSeleccionadaComoString() {
        if (this.getDate() == null) {
            return null; // Evita un NullPointerException si no hay fecha
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.getDate());
    }

}