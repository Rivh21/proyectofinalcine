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
public class ListaSimple<T> {

    private Nodo lista;

    public ListaSimple() {
        this.lista = null;
    }

    public boolean isEmpty() {
        return lista == null;
    }

     public <T extends Comparable> void insertar(T dato) {
        Nodo nodito = new Nodo(dato);
        if (isEmpty()) {
            lista = nodito;
        } else if (dato.compareTo(lista.getDato()) < 0) {
            nodito.setSiguiente(lista);
            lista = nodito;
        } else {
            Nodo anterior = ubicar(dato);
            nodito.setSiguiente(anterior.getSiguiente());
            anterior.setSiguiente(nodito);
        }
    }


    public <T extends Comparable> Nodo ubicar(T dato) {
        Nodo aux = lista;
        Nodo anterior = lista;
        while (aux.getSiguiente() != null && dato.compareTo(aux.getDato()) > 0) {
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
                lista = lista.getSiguiente();
            } else {
                anterior.setSiguiente(quitar.getSiguiente());
            }
            quitar = null;
        }
    }

    //to array sirve para ver todo el contenido
    public ArrayList<T> toArray() {
        ArrayList<T> temp = new ArrayList();
        Nodo<T> aux = lista;

        while (aux != null) {
            temp.add(aux.getDato());
            aux = aux.getSiguiente();
        }
        return temp;
    }

    public <T extends Comparable> Nodo buscar(T dato) {
        Nodo<T> aux = lista;

        while (aux != null) {
            if (dato.compareTo(aux.getDato()) == 0) {
                return aux;
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public T get(int index) {
        Nodo<T> aux = lista;
        int contador = 0;

        while (aux != null) {
            if (contador == index) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
            contador++;
        }

        throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
    }
}
