/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author DELL LATITUDE
 */
public class Usuario implements Comparable<Usuario> {

    private int idUsuario;
    private String nombreUsuario;
    private String password;
    private Rol rol;
    private Empleado empleado;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String password, Rol rol, Empleado empleado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.rol = rol;
        this.empleado = empleado;
    }

    public Usuario(String nombreUsuario, String password, Rol rol, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.rol = rol;
        this.empleado = empleado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public int compareTo(Usuario o) {
        Usuario actual = this;
        return (actual.getNombreUsuario().compareToIgnoreCase(o.getNombreUsuario()));
    }

}
