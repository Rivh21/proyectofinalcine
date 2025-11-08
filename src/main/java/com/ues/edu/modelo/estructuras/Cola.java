/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.estructuras;

import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Cola<T> {

    private Nodo<T> fr;
    private Nodo<T> fn;

    public Cola() {
        this.fr = null;
        this.fn = null;
    }

    public boolean isEmpty() {
        return (fr == null && fn == null);

    }

    public void offer(T dato) {
        Nodo nodito = new Nodo(dato);
        if (isEmpty()) {
            fr = nodito;
        } else {
            fn.setSiguiente(nodito);
        }
        fn = nodito;
    }

    public ArrayList<T> toArray() {
        ArrayList<T> array = new ArrayList();
        Nodo aux = fr;
        while (aux != null) {
            array.add((T) aux.getDato());
            aux = aux.getSiguiente();
        }
        return array;
    }

    public T poll() {
        if (isEmpty()) {
            return (T) "Cola no tiene datos";
        }
        T dato = fr.getDato();
        if (fr.getSiguiente() == null) {
            fr = fn = null;
        } else {
            fr = fr.getSiguiente();
        }
        return dato;
    }

    public T peek() {
        T cima = (T) "vacio";
        if (!isEmpty()) {
            cima = fr.getDato();
        }
        return cima;
    }
   
}
