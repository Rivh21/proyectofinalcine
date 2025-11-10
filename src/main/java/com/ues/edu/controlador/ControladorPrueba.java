/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.GenericTableModel;
import com.ues.edu.vista.ModalAsientos;
import com.ues.edu.vista.ModalPeliculas;
import com.ues.edu.vista.VistaPrueba;
import com.ues.edu.vista.swing.TablaConCheckboxes;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jorge
 */
public class ControladorPrueba {

    private final VistaPrueba vista;

    public ControladorPrueba(VistaPrueba vista) {
        this.vista = vista;
        pintarTabla();
        vista.jLabel1.setText("PRUEBA DE CONTROLADOR");
        btn();
    }

    private void pintarTabla() {
        String[] nombres = {"Rol", "Permiso", "Tiene Permiso"};
        Class<?>[] tipos = {String.class, String.class, Boolean.class}; 
        boolean[] editable = {false, false, true}; 
        
        List<Object[]> datos = Arrays.asList(
            new Object[]{"nombreRol", "permiso", false},
            new Object[]{"Admin", "Gestion interna", false},
            new Object[]{"Admin", "Producto", false},
            new Object[]{"Admin", "Ventas", false}
        );

        //nombreRol = rolSelect.getNombreRol();
        //permiso = permisoSelect.getNombrePermiso();
        //validacion pendiente para no utilizar permisos repetidos en un mismo rol
        if (vista.miTablaConCheckboxes1 != null) {

            GenericTableModel modeloConfigurado = new GenericTableModel(
                nombres, 
                tipos, 
                editable, 
                datos
            );

            vista.miTablaConCheckboxes1.setModel(modeloConfigurado);

            
        } else {
            System.err.println("La tabla miTablaConCheckboxes1 no está disponible para configuración.");
        }
    }

    private void btn() {
        vista.jButton1.addActionListener((e)->{
            ModalAsientos ma = new ModalAsientos(new JFrame(), true, "Generar asientos");
            ma.setVisible(true);
        });
    }
}
