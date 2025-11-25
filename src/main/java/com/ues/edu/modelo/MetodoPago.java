/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author DELL LATITUDE
 */
public class MetodoPago implements Comparable<MetodoPago>{
    private int idMetodoPago;
    private String nombreMetodo;

    public MetodoPago() {
    }

    public MetodoPago(int idMetodoPago, String nombreMetodo) {
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodo = nombreMetodo;
    }

    public MetodoPago(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

   
    public int getidMetodoPago() {
        return idMetodoPago;
    }

    public void setidMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getnombreMetodo() {
        return nombreMetodo;
    }

    public void setnombreMetodo(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

    @Override
    public String toString() {
        return nombreMetodo;
    }

    @Override
    public int compareTo(MetodoPago o) {
         MetodoPago actual = this;
      return (actual.getnombreMetodo().compareToIgnoreCase(o.getnombreMetodo()));
    }
    public MetodoPago(int idMetodoPago) {
    this.idMetodoPago = idMetodoPago;
}

}
