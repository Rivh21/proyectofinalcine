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
public class ArbolBPadre {

    Nodo1 raiz;

    public ArbolBPadre() {
        this.raiz = null;
    }

    public boolean isEmpty() {
        return this.raiz == null;
    }

    public Nodo1 getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo1 raiz) {
        this.raiz = raiz;
    }

    protected ArrayList preOrdenNID(Nodo1 r, ArrayList a) {
        if (r != null) {
            a.add(r.getDato());
            preOrdenNID(r.getIzdo(), a);
            preOrdenNID(r.getDrcho(), a);
        }
        return a;
    }

    protected ArrayList inOrdenIND(Nodo1 r, ArrayList a) {
        if (r != null) {
            inOrdenIND(r.getIzdo(), a);
            a.add(r.getDato());
            inOrdenIND(r.getDrcho(), a);
        }
        return a;
    }

    protected ArrayList pstOrdenIDN(Nodo1 r, ArrayList a) {
        if (r != null) {
            pstOrdenIDN(r.getIzdo(), a);
            pstOrdenIDN(r.getDrcho(), a);
            a.add(r.getDato());
        }
        return a;
    }

    public <T extends Comparable> Nodo1 buscar(T dato) {
        return buscar(dato, raiz);
    }

    private <T extends Comparable> Nodo1 buscar(T dato, Nodo1 r) {
        if (r == null) {
            return null;
        } else if (dato.compareTo(r.getDato()) == 0) {
            return r;
        } else if (dato.compareTo(r.getDato()) < 0) {
            return buscar(dato, r.getIzdo());
        } else {
            return buscar(dato, r.getDrcho());
        }
    }

    public int altura(Nodo1 q) {
        if (q == null) {
            return 0;
        } else if (isHoja1(q)) {
            return 1;
        } else {
            int a1 = (q.getIzdo() == null) ? 0 : altura(q.getIzdo());
            int a2 = (q.getDrcho() == null) ? 0 : altura(q.getDrcho());
            return 1 + Math.max(a1, a2);

        }
    }

    public boolean isHoja1(Nodo1 q) {
        if (q.getIzdo() == null && q.getDrcho() == null) {
            return true;
        } else {
            return false;
        }
    }
}
