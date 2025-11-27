/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.utilidades;

import java.util.Random;

/**
 *
 * @author jorge
 */
public class GeneradorID {

    public static String generarCodigoFactura() {
        // Ejemplo de formato: "FAC-A1B2"
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("FAC-"); // Prefijo
        Random r = new Random();

        // Genera 6 caracteres aleatorios
        for (int i = 0; i < 6; i++) {
            int index = r.nextInt(letras.length());
            sb.append(letras.charAt(index));
        }

        return sb.toString(); // Retorna la factura "FAC-9X2J1M"
    }
}
