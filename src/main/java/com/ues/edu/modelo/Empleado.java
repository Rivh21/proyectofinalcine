/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author DELL LATITUDE
 */
public class Empleado implements Comparable<Empleado> {

    private int idEmpleado;
    private String nombre;
    private String apellido;
    private String dui;
    private String email;
    private String telefono;
    private Double salario;

    public Empleado() {
    }

    public Empleado(int idEmpleado, String nombre, String apellido, String dui, String email, String telefono, Double salario) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dui = dui;
        this.email = email;
        this.telefono = telefono;
        this.salario = salario;
    }

    public Empleado(String nombre, String apellido, String dui, String email, String telefono, Double salario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dui = dui;
        this.email = email;
        this.telefono = telefono;
        this.salario = salario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @Override
    public int compareTo(Empleado o) {
        Empleado actual = this;
        return (actual.getNombre().compareToIgnoreCase(o.getNombre()));
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellido;
    }
    public Empleado(int idEmpleado) {
    this.idEmpleado = idEmpleado;
}

}
