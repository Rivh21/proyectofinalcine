/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.estructuras;

/**
 *
 * @author radon
 */
public class Nodo1<T> {
    private T dato;
    private Nodo1 izdo;
    private Nodo1 drcho;
    int alt;

    public Nodo1(T dato) {
        this.dato = dato;
        this.izdo=null;
        this.drcho=null;
        this.alt=0;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo1 getIzdo() {
        return izdo;
    }

    public void setIzdo(Nodo1 izdo) {
        this.izdo = izdo;
    }

    public Nodo1 getDrcho() {
        return drcho;
    }

    public void setDrcho(Nodo1 drcho) {
        this.drcho = drcho;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    @Override
    public String toString() {
        return  dato + "";
    }
    
    
    

    
    
}
