/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.vista.ModalAsientos;
import com.ues.edu.vista.ModalPeliculas;
import com.ues.edu.vista.VistaPrueba;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jorge
 */
public class ControladorPrueba {

    private final VistaPrueba vista;
    private DefaultTableModel modelo;

    public ControladorPrueba(VistaPrueba vista) {
        this.vista = vista;
        pintarTabla();
        vista.jLabel1.setText("PRUEBA DE CONTROLADOR");
        btn();
    }

    private void pintarTabla() {
        modelo = new DefaultTableModel();
        String titulos[] = {"Prueba", "Controlador"};
        modelo.setColumnIdentifiers(titulos);
        modelo.addRow(new Object[]{"Dato 1", "Controlador A"});
        modelo.addRow(new Object[]{"Dato 2", "Controlador B"});
        vista.tbDatosPrueba.setModel(modelo);

    }

    private void btn() {
        vista.jButton1.addActionListener((e)->{
            ModalAsientos ma = new ModalAsientos(new JFrame(), true, "Generar asientos");
            ma.setVisible(true);
        });
    }
}
