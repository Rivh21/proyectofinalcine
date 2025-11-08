/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Permiso implements Comparable<Permiso>{

    private int idPermiso;
    private String nombrePermiso;
    private ArrayList<PermisoRol> permisosRoles;

    public Permiso() {
    }

    public Permiso(int idPermiso, String nombrePermiso) {
        this.idPermiso = idPermiso;
        this.nombrePermiso = nombrePermiso;
    }

    public Permiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public ArrayList<PermisoRol> getPermisosRoles() {
        return permisosRoles;
    }

    public void setPermisosRoles(ArrayList<PermisoRol> permisosRoles) {
        this.permisosRoles = permisosRoles;
    }

    @Override
    public int compareTo(Permiso o) {
        Permiso actual = this;
        return (actual.getNombrePermiso()).compareToIgnoreCase(o.getNombrePermiso());
    }

}
