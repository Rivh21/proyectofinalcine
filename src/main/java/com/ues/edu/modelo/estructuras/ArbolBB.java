/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.estructuras;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author radon
 */
public class ArbolBB extends ArbolBPadre{

    public ArbolBB() {
        super();
    }
    public <T extends Comparable>void insertar(T dato){
        super.setRaiz(insertar(dato, super.getRaiz()));
    }

    private <T extends Comparable> Nodo1 insertar(T dato, Nodo1 r) {
        if(r==null){
            r= new Nodo1(dato);
        }else if(dato.compareTo(r.getDato())<0){
            Nodo1 izq;
            izq= insertar(dato, r.getIzdo());
            r.setIzdo(izq);
        }else{
            if(dato.compareTo(r.getDato())> 0){
            Nodo1 drch;
            drch= insertar(dato, r.getDrcho());
            r.setDrcho(drch);
        }else{
                JOptionPane.showMessageDialog(null, "Duplicado");
            }
    }
    return r;

   
    }
    
    
    public ArrayList recorridoIND(){
        ArrayList a= new ArrayList();
        return super.inOrdenIND(super.getRaiz(), a);
    }
    public ArrayList recorridoNID(){
        ArrayList a= new ArrayList();
        return super.preOrdenNID(super.getRaiz(), a);
    }
    public ArrayList recorridoIDN(){
        ArrayList a= new ArrayList();
        return super.pstOrdenIDN(super.getRaiz(), a);
    }
    
    
    public <T extends Comparable>void eliminar(T dato){
        super.setRaiz(eliminar(dato, super.getRaiz()));
    }

    private <T extends Comparable> Nodo1 eliminar(T dato, Nodo1 r) {
        if(r== null){
            JOptionPane.showMessageDialog(null, "No hay un nodo a eliminar");
        }else if(dato.compareTo(r.getDato())<0){
            Nodo1 izq;
            izq= eliminar(dato, r.getIzdo());
            r.setIzdo(izq);
        }else if(dato.compareTo(r.getDato())>0){
            Nodo1 drcho;
            drcho= eliminar(dato, r.getDrcho());
            r.setDrcho(drcho);
        }else{
            Nodo1 q;
            q= r;
            if(q.getIzdo()==null){ //solo tiene un hijo derechp
                r= q.getDrcho();
            }else if(q.getDrcho()==null){ //solo tiene un hijo izquierdo
                r= q.getIzdo();
            }else{ //hay dos hijos o dos ramas
                q= reemplazar(q); // si llega a este metodo es xq tiene dos hijos
            }
            q=null;
        }
        return r;
    }

    private Nodo1 reemplazar(Nodo1 actual) {
        Nodo1 aux, ant;
        ant= actual;
        aux= actual.getIzdo();//para reemplazar por el que esta mas a a la derecha
        while(aux.getDrcho()!=null){//de la rama izquierda
            ant = aux;
            ant= aux.getDrcho();
        }
        actual.setDato(aux.getDato());
        if(ant==actual){
            ant.setIzdo(aux.getIzdo());
        }else{
            ant.setDrcho(aux.getIzdo());
        }
        return aux;
    }
}
