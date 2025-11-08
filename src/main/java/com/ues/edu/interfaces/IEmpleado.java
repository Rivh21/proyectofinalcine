/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;


import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IEmpleado {
    public ListaSimple<Empleado> selectAll();
    public ListaSimple<Empleado> selectAllTo(String atributo, String condicion);
    public ListaSimple<Empleado> buscar (String dato);
    public ListaSimple<Empleado> selectEmpleadosSinUsuario();
    public boolean insert (Empleado obj);
    public boolean update (Empleado obj);
    public boolean delete (Empleado obj);
    public boolean existeDui (String dui);
    
}
