/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.utilidades;

import java.util.regex.Pattern;

/**
 *
 * @author jorge
 */
public class Validaciones {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public boolean validarEmail(String email) {
        // Un correo nulo o vacío no es válido
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Compara el email con el patrón de la expresión regular
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
