/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author jorge
 */
public class PermisoRol implements Comparable<PermisoRol> {
    
    private int idPermisoRol;
    private Rol rol;
    private Permiso permiso;
    private Boolean tienePermiso;
    
    public PermisoRol() {
    }
    
    public PermisoRol(int idPermisoRol, Rol rol, Permiso permiso, Boolean tienePermiso) {
        this.idPermisoRol = idPermisoRol;
        this.rol = rol;
        this.permiso = permiso;
        this.tienePermiso = tienePermiso;
    }
    
    public PermisoRol(Rol rol, Permiso permiso, Boolean tienePermiso) {
        this.rol = rol;
        this.permiso = permiso;
        this.tienePermiso = tienePermiso;
    }
    
    public int getIdPermisoRol() {
        return idPermisoRol;
    }
    
    public void setIdPermisoRol(int idPermisoRol) {
        this.idPermisoRol = idPermisoRol;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public Permiso getPermiso() {
        return permiso;
    }
    
    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
    
    public Boolean getTienePermiso() {
        return tienePermiso;
    }
    
    public void setTienePermiso(Boolean tienePermiso) {
        this.tienePermiso = tienePermiso;
    }
    
    @Override
    public int compareTo(PermisoRol o) {
        PermisoRol actual = this;
        return (actual.getRol().getNombreRol().compareToIgnoreCase(o.getRol().getNombreRol()));
    }
    
}
