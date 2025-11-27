package com.ues.edu.modelo;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.utilidades.GeneradorID;

public class FacturaConcesion implements Comparable<FacturaConcesion> {

    private String idFacturaConcesion;
    private Double montoTotal;
    private MetodoPago metodoPago;
    private Empleado empleado;
    private ListaSimple<DetalleConcesion> detalleConcesion;

    public FacturaConcesion() {
        detalleConcesion = new ListaSimple<>();
        this.idFacturaConcesion = GeneradorID.generarCodigoFactura();
    }

    public String getIdFacturaConcesion() {
        return idFacturaConcesion;
    }

    public void setIdFacturaConcesion(String idFacturaConcesion) {
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

    public ListaSimple<DetalleConcesion> getDetalleConcesion() {
        return detalleConcesion;
    }

    public void setDetalleConcesion(ListaSimple<DetalleConcesion> detalleConcesion) {
        this.detalleConcesion = detalleConcesion;
    }

    @Override
    public int compareTo(FacturaConcesion o) {
        if (this.idFacturaConcesion == null || o.getIdFacturaConcesion() == null) {
             return 0; 
        }
        return this.idFacturaConcesion.compareTo(o.getIdFacturaConcesion());
    }
}