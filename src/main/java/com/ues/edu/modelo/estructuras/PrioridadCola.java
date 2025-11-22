/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.estructuras;

/**
 *
 * @author radon
 */
import java.util.ArrayList;

public class PrioridadCola<T> {

    private ArrayList<Nodo> cola;

    public PrioridadCola(int pr) {
        cola = new ArrayList();
        for (int i = 0; i < pr; i++) {
            cola.add(null);
        }
    }

    public boolean isEmpty() {
        int i = cola.size();
        int cont = 0;
        while (cont < i) {
            if (cola.get(cont) != null) {
                return false;
            }
            cont++;
        }
        return true;
    }

    public void offer(T dato, int pr) {
        Nodo nodito = new Nodo(dato);
        Nodo fr = cola.get(pr);
        Nodo fn = cola.get(pr);
        if (fr == null) {
            cola.set(pr, nodito);
        } else {
            while (fn.getSiguiente() != null) {
                fn = fn.getSiguiente();
            }
            fn.setSiguiente(nodito);
        }
    }

    public ArrayList toArray(int pr) {
        ArrayList<T> aux = new ArrayList();
        Nodo fr = cola.get(pr);
        while (fr != null) {
            aux.add((T) fr.getDato());
            fr = fr.getSiguiente();
        }
        return aux;
    }

    public ArrayList toArray() {
        ArrayList<T> aux = new ArrayList();
        if (!isEmpty()) {
            for (int i = 0; i < cola.size(); i++) {
                Nodo fr = cola.get(i);
                while (fr != null) {
                    aux.add((T) fr.getDato());
                    fr = fr.getSiguiente();
                }
            }
        }
        return aux;
    }
    
    public T poll(){
     if (isEmpty()) {
            return (T) "Cola no tiene datos";
        }
     int pr = prioridad();
     Nodo fr = cola.get(pr);
     T dato = (T) fr.getDato();
     fr = fr.getSiguiente();
     cola.set(pr, fr);
     return dato;
    } 

    private int prioridad() {
        for (int i = 0; i < cola.size(); i++) {
            if (cola.get(i)!=null) {
                return i;
            }
        }
        return -1;
    }
    
}
