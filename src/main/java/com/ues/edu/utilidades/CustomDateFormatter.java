/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class CustomDateFormatter {

    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");

    //Mapa para días de la semana
    private static final Map<Long, String> DAYS_OF_WEEK = new HashMap<>();

    static {
        DAYS_OF_WEEK.put(1L, "LUN.");
        DAYS_OF_WEEK.put(2L, "MAR.");
        DAYS_OF_WEEK.put(3L, "MIÉ.");
        DAYS_OF_WEEK.put(4L, "JUE.");
        DAYS_OF_WEEK.put(5L, "VIE.");
        DAYS_OF_WEEK.put(6L, "SÁB.");
        DAYS_OF_WEEK.put(7L, "DOM.");
    }

    //Mapa para meses
    private static final Map<Long, String> MONTHS = new HashMap<>();

    static {
        MONTHS.put(1L, "ENE");
        MONTHS.put(2L, "FEB");
        MONTHS.put(3L, "MAR");
        MONTHS.put(4L, "ABR");
        MONTHS.put(5L, "MAY");
        MONTHS.put(6L, "JUN");
        MONTHS.put(7L, "JUL");
        MONTHS.put(8L, "AGO");
        MONTHS.put(9L, "SEP");
        MONTHS.put(10L, "OCT");
        MONTHS.put(11L, "NOV");
        MONTHS.put(12L, "DIC");
    }

    public static final DateTimeFormatter WEEKDAY_DATE_TIME = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendText(ChronoField.DAY_OF_WEEK, DAYS_OF_WEEK)
            .appendLiteral(' ')
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(' ')
            .appendText(ChronoField.MONTH_OF_YEAR, MONTHS)
            .appendLiteral(", ")
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter(SPANISH_LOCALE);

    //Métodos utilitarios
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(WEEKDAY_DATE_TIME);
    }
}
