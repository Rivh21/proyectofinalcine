/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.estructuras;

import java.util.ArrayList;

/**
 *
 * @author radon
 */
public class ListaSimpleCircular<T> {

    private Nodo lista;

    public ListaSimpleCircular() {
        this.lista = null;
    }

    public boolean isEmpty() {
        return lista == null;
    }

    public <T extends Comparable> Nodo ubicar(T dato) {
        Nodo aux = lista;
        Nodo anterior = lista;
        while (aux.getSiguiente() != lista // este cambio se hizo enves de null llamas a la lista
                && dato.compareTo(aux.getDato()) > 0) {
            anterior = aux;
            aux = aux.getSiguiente();
        }
        if (dato.compareTo(aux.getDato()) > 0) {
            anterior = aux;
        }
        return anterior;
    }

    public <T extends Comparable> void eliminar(T dato) {
        Nodo quitar = buscar(dato);
        if (quitar != null) {
            Nodo anterior = ubicar(dato);

            if (quitar == lista) {
                if (lista.getSiguiente() != lista) {
                    Nodo ultimo = ultimo();
                    lista = lista.getSiguiente();
                    ultimo.setSiguiente(lista);
                } else {
                    lista = null;
                }
            } else {
                anterior.setSiguiente(quitar.getSiguiente());
            }

            quitar = null;
        }
    }

    public <T extends Comparable> Nodo buscar(T dato) {
        Nodo aux = lista;
        if (!isEmpty()) {
            do {
                if (dato.compareTo(aux.getDato()) == 0) {
                    return aux;
                }
                aux = aux.getSiguiente();
            } while (aux != lista);
        }
        return null;
    }

    public ArrayList<T> toArray() {
        Nodo aux = lista;
        ArrayList array = new ArrayList();
        if (!isEmpty()) {
            do {
                array.add(aux.getDato());
                aux = aux.getSiguiente();
            } while (aux != lista);
        }

        return array;
    }

    public <T extends Comparable> void insertar(T dato) {
        Nodo nodito = new Nodo(dato);
        if (isEmpty()) {
            lista = nodito;
            nodito.setSiguiente(lista);// 1-cambio 1
        } else if (dato.compareTo(lista.getDato()) < 0) {
            Nodo ultimo = ultimo(); //2-cambio 
            nodito.setSiguiente(lista);
            lista = nodito;
            ultimo.setSiguiente(lista);
        } else {
            Nodo anterior = ubicar(dato);//cambio
            nodito.setSiguiente(anterior.getSiguiente());
            anterior.setSiguiente(nodito);

        }
    }

    private Nodo ultimo() {
        Nodo aux = lista;
        while (aux.getSiguiente() != lista) {
            aux = aux.getSiguiente();
        }
        return aux;
    }
    
 

}
