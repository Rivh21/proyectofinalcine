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
public class Pila<T> {

    private Nodo<T> pilita;

    public Pila() {
        this.pilita = null;
    }

    //inserta en la pila
    public void push(T dato) {
        Nodo nodito = new Nodo(dato, pilita);
//        nodito.setSiguiente(pilita);
        pilita = nodito;
    }
    
    //borra el ultimo en entrar a la pila
    public T pop() {
        //solo se guarda en el dato | |/|, no el nodo completo
        T cima = (T) "vacío";
        if (!isEmpty()) {
            cima = pilita.getDato();
            pilita = pilita.getSiguiente();
        }
        return cima;
    }

    //muestra el que esta arriba de la pila
    public T peek() {
        T cima = (T) "vacío";
        if (!isEmpty()) {
            cima = pilita.getDato();
        }
        return cima;
    }

    public boolean isEmpty() {
        return pilita == null;
    }

    //to array sirve para ver todo el contenido
    public ArrayList<T> toArray() {
        ArrayList<T> temp = new ArrayList();
        Nodo aux = pilita;

        while (aux != null) {
            temp.add((T) aux.getDato());
            aux = aux.getSiguiente();
        }
        return temp;
    }
}
