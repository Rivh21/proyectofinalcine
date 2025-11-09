package com.ues.edu.controlador;

import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.VistaListado;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class ControladorListadoPeliculas {

    private final ControladorModalFunciones cmf;
    private final VistaListado vistaLista;
    private final PeliculaDAO daoPelicula;
    private Pelicula peliculaSelect;
    private ListaSimpleCircular<Pelicula> listaActualMostrada;
    private DefaultTableModel modelo;

    public ControladorListadoPeliculas(ControladorModalFunciones cmf, VistaListado vistaLista) {
        this.cmf = cmf;
        this.vistaLista = vistaLista;
        this.daoPelicula = new PeliculaDAO();
        this.listaActualMostrada = daoPelicula.selectAll();
        vistaLista.btnSeleccionar.setEnabled(false);

        keyReleasedBuscar();
        onClickTabla();
        onClickSeleccionar();
        mostrar(listaActualMostrada);
    }

    private void onClickSeleccionar() {
        vistaLista.btnSeleccionar.addActionListener(e -> {
            if (peliculaSelect != null) {
                cmf.cargarPelicula(peliculaSelect);
                vistaLista.dispose();
            }
        });
    }

    private void onClickTabla() {
        vistaLista.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowVista = vistaLista.tbDatos.getSelectedRow();
                if (rowVista >= 0) {
                    int rowModelo = vistaLista.tbDatos.convertRowIndexToModel(rowVista);
                    Object idValue = vistaLista.tbDatos.getModel().getValueAt(rowModelo, 0);
                    int idPelicula = (Integer) idValue;

                    peliculaSelect = null;
                    for (Pelicula p : listaActualMostrada.toArray()) {
                        if (p.getIdPelicula() == idPelicula) {
                            peliculaSelect = p;
                            break;
                        }
                    }

                    vistaLista.btnSeleccionar.setEnabled(peliculaSelect != null);
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        vistaLista.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = vistaLista.tfBuscar.getText().trim().toLowerCase();

                ListaSimpleCircular<Pelicula> listaFiltrada = new ListaSimpleCircular<>();
                for (Pelicula p : daoPelicula.selectAll().toArray()) {
                    if (p.getTitulo().toLowerCase().contains(textoBusqueda)) {
                        listaFiltrada.insertar(p);
                    }
                }
                mostrar(listaFiltrada);
            }
        });
    }

    public void mostrar(ListaSimpleCircular<Pelicula> lista) {
        listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "TÍTULO", "DURACIÓN", "GÉNERO", "CLASIFICACIÓN"};
        modelo.setColumnIdentifiers(titulos);

        for (Pelicula p : lista.toArray()) {
            Object[] fila = {
                p.getIdPelicula(),
                p.getTitulo(),
                p.getDuracionMinutos(),
                p.getGenero(),
                p.getClasificacion()
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        vistaLista.tbDatos.setRowSorter(sorter);
        vistaLista.tbDatos.setModel(modelo);

        int[] anchos = {30, 200, 80, 100, 80};
        TableColumnModel columnModel = vistaLista.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}
