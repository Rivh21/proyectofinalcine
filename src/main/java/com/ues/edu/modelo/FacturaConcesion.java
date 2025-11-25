package com.ues.edu.modelo;

import com.ues.edu.modelo.estructuras.ListaSimple;


public class FacturaConcesion implements Comparable<FacturaConcesion> {

    private int idFacturaConcesion;
    private Double montoTotal;
    private MetodoPago metodoPago;
    private Empleado empleado;
    private ListaSimple<DetalleConcesion> detalleConcesion;

    public FacturaConcesion() {
        detalleConcesion = new ListaSimple<>();
    }

    public int getIdFacturaConcesion() {
        return idFacturaConcesion;
    }

    public void setIdFacturaConcesion(int idFacturaConcesion) {
        this.idFacturaConcesion = idFacturaConcesion;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    // ELIMINADO: getFecha() y setFecha(LocalDateTime fecha)
    // El atributo 'fecha' ya no existe

    public ListaSimple<DetalleConcesion> getDetalleConcesion() {
        return detalleConcesion;
    }

    public void setDetalleConcesion(ListaSimple<DetalleConcesion> detalleConcesion) {
        this.detalleConcesion = detalleConcesion;
    }

    @Override
    public int compareTo(FacturaConcesion o) {
        // CORREGIDO: Como 'fecha' fue eliminada, usamos 'idFacturaConcesion' para Comparable.
        return Integer.compare(this.idFacturaConcesion, o.getIdFacturaConcesion());
    }
}