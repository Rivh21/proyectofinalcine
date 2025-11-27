/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.ArrayList;

/**
 *
 * @author radon
 */
public class Rol implements Comparable<Rol> {

    private int idRol;
    private String nombreRol;
    private ArrayList<Usuario> usuarios;
    private ArrayList<PermisoRol> permisosRoles;

    public Rol() {
    }

    public Rol(int idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public ArrayList<PermisoRol> getPermisosRoles() {
        return permisosRoles;
    }

    public void setPermisosRoles(ArrayList<PermisoRol> permisosRoles) {
        this.permisosRoles = permisosRoles;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Rol(ArrayList<PermisoRol> permisosRoles) {
        this.permisosRoles = permisosRoles;
    }

    @Override
    public String toString() {
        return this.nombreRol;
    }

    @Override
    public int compareTo(Rol o) {
        Rol actual = this;
        return (actual.getNombreRol().compareToIgnoreCase(o.getNombreRol()));
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idRol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Rol rol = (Rol) obj;

        return idRol == rol.idRol;
    }

}
