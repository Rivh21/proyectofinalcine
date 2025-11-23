/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.AsientoDao;
import com.ues.edu.modelo.dao.BoletoDao;
import com.ues.edu.modelo.dao.FuncionDAO;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.modelo.estructuras.Pila;
import com.ues.edu.vista.ModalFactura;
import com.ues.edu.vista.VistaBoletos;
import com.ues.edu.vista.VistaListado;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jorge
 */
public class ControladorBoletos implements ActionListener {

    private VistaBoletos vista;
    private final Usuario usuarioLogueado;

    private BoletoDao boletoDao;
    private AsientoDao asientoDao;
    private FuncionDAO funcionDao;
    private Funcion funcionActual;

    private Pila<Asiento> asientosSeleccionadosCompra;
    private List<Asiento> listaAsientosActuales;

    private final Color COLOR_DISPONIBLE = new Color(50, 200, 50);
    private final Color COLOR_OCUPADO = new Color(220, 50, 50);
    private final Color COLOR_SELECCIONADO = new Color(255, 165, 0);

    public ControladorBoletos(VistaBoletos vista, Usuario usuarioLogueado) {
        this.vista = vista;
        this.usuarioLogueado = usuarioLogueado;

        this.boletoDao = new BoletoDao();
        this.asientoDao = new AsientoDao();
        this.funcionDao = new FuncionDAO();

        this.asientosSeleccionadosCompra = new Pila<>();
        this.listaAsientosActuales = new ArrayList<>();

        onClickSelectFuncion();
        onClickFactura();

        this.vista.lbPantalla.setVisible(false);
        this.vista.lbTotal.setText("");
        this.vista.lbSala.setText("");
        this.vista.lbFuncion.setText("");
    }

    public void cargarFuncion(Funcion funcion) {
        this.funcionActual = funcion;

        if (this.funcionActual != null) {
            this.vista.lbPantalla.setVisible(true);
            this.vista.lbTotal.setText("Total: $ 0.00");
            this.vista.lbSala.setText("SALA: " + funcionActual.getSalaNombre());
            this.vista.lbFuncion.setText("FUNCIÓN: " + funcionActual.getPeliculaTitulo().toUpperCase());
            generarBotonesAsientos();
        } else {
            this.vista.lbPantalla.setVisible(false);
            this.vista.panelAsientos.removeAll();
            this.vista.panelAsientos.repaint();
        }
    }

    private void generarBotonesAsientos() {
        vista.panelAsientos.removeAll();

        ListaSimple<Asiento> listaEstructura = asientoDao.selectBySala(funcionActual.getIdSala());

        this.listaAsientosActuales = convertirLista(listaEstructura);
        Collections.sort(this.listaAsientosActuales);

        HashSet<Integer> ocupados = boletoDao.getAsientosOcupados(funcionActual.getIdFuncion());

        asientosSeleccionadosCompra = new Pila<>();
        calcularTotal();

        if (this.listaAsientosActuales.isEmpty()) {
            vista.panelAsientos.revalidate();
            vista.panelAsientos.repaint();
            return;
        }

        int maxColumna = 0;
        for (Asiento a : this.listaAsientosActuales) {
            if (a.getNumero() > maxColumna) {
                maxColumna = a.getNumero();
            }
        }
        int filasEstimadas = (int) Math.ceil((double) this.listaAsientosActuales.size() / maxColumna);

        vista.panelAsientos.setLayout(new GridLayout(filasEstimadas, maxColumna, 8, 8));

        for (Asiento asiento : this.listaAsientosActuales) {
            JButton btn = new JButton(asiento.getFila() + "-" + asiento.getNumero());
            btn.setName(String.valueOf(asiento.getIdAsiento()));
            btn.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 11));

            if (ocupados.contains(asiento.getIdAsiento())) {
                btn.setBackground(COLOR_OCUPADO);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(COLOR_DISPONIBLE);
                btn.setForeground(Color.WHITE);
                btn.setEnabled(true);
                btn.addActionListener(this);
                btn.setActionCommand("SELECCIONAR_ASIENTO");
            }
            vista.panelAsientos.add(btn);
        }

        vista.panelAsientos.revalidate();
        vista.panelAsientos.repaint();
    }

    private void onClickSelectFuncion() {
        vista.btnSelectFuncion.addActionListener((e) -> {
            VistaListado vistaModal = new VistaListado(new JFrame(), true, "Funciones disponibles");
            ControladorSeleccionarFuncion csf = new ControladorSeleccionarFuncion(this, vistaModal);
            vistaModal.setVisible(true);
        });
    }

    private void onClickFactura() {
        vista.btnFactura.addActionListener(e -> {
            if (asientosSeleccionadosCompra.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione al menos un asiento.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ModalFactura modal = new ModalFactura(new JFrame(), true, "Confirmar Compra");

            ControladorFactura cf = new ControladorFactura(
                    modal,
                    this.usuarioLogueado,
                    this.funcionActual,
                    this.asientosSeleccionadosCompra
            );

            modal.setVisible(true);

            if (cf.isVentaRealizada()) {
                cargarFuncion(funcionActual);
                vista.lbTotal.setText("Total: $ 0.00");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SELECCIONAR_ASIENTO")) {
            JButton btn = (JButton) e.getSource();
            int idAsiento = Integer.parseInt(btn.getName());

            if (btn.getBackground().equals(COLOR_DISPONIBLE)) {
                btn.setBackground(COLOR_SELECCIONADO);
                agregarAlCarrito(idAsiento);
            } else if (btn.getBackground().equals(COLOR_SELECCIONADO)) {
                btn.setBackground(COLOR_DISPONIBLE);
                removerDelCarrito(idAsiento);
            }
        }
    }

    private void agregarAlCarrito(int idAsiento) {
        Asiento asientoCompleto = null;

        if (listaAsientosActuales != null) {
            for (Asiento a : listaAsientosActuales) {
                if (a.getIdAsiento() == idAsiento) {
                    asientoCompleto = a;
                    break;
                }
            }
        }

        if (asientoCompleto != null) {
            asientosSeleccionadosCompra.push(asientoCompleto);
        } else {
            Asiento a = new Asiento();
            a.setIdAsiento(idAsiento);
            asientosSeleccionadosCompra.push(a);
        }

        calcularTotal();
    }

    private void removerDelCarrito(int idAsiento) {
        Pila<Asiento> pilaAuxiliar = new Pila<>();

        while (!asientosSeleccionadosCompra.isEmpty()) {
            Asiento a = asientosSeleccionadosCompra.pop();
            if (a.getIdAsiento() == idAsiento) {
                break;
            } else {
                pilaAuxiliar.push(a);
            }
        }

        while (!pilaAuxiliar.isEmpty()) {
            asientosSeleccionadosCompra.push(pilaAuxiliar.pop());
        }
        calcularTotal();
    }

    private void calcularTotal() {
        if (funcionActual != null) {
            ArrayList<Asiento> lista = asientosSeleccionadosCompra.toArray();
            double total = lista.size() * funcionActual.getPrecioBoleto();
            this.vista.lbTotal.setText("Total: $" + String.format("%.2f", total));
        }
    }

    private List<Asiento> convertirLista(ListaSimple<Asiento> listaSimple) {
        List<Asiento> lista = new ArrayList<>();
        ArrayList<Asiento> array = listaSimple.toArray();
        if (array != null) {
            for (Object o : array) {
                lista.add((Asiento) o);
            }
        }
        return lista;
    }
}
