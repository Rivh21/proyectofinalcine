package com.ues.edu.controlador;

/**
 *
 * @author radon
 */
import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.dao.PeliculaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalPeliculas;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class ControladorPeliculas {

    Mantenimiento mantto;
    DefaultTableModel modelo;
    Pelicula peliculaSelect;
    PeliculaDAO daoPelicula;
    private ListaSimpleCircular<Pelicula> listaActualMostrada;

    public ControladorPeliculas(Mantenimiento mantto) {
        daoPelicula = new PeliculaDAO();
        this.mantto = mantto;
        this.listaActualMostrada = daoPelicula.selectAll(); // ← cambio
        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener((e) -> {
            ModalPeliculas mp = new ModalPeliculas(new JFrame(), true, "Agregar Película");
            ControladorModalPeliculas cmp = new ControladorModalPeliculas(this, mp);
            mp.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener((e) -> {
            ModalPeliculas mp = new ModalPeliculas(new JFrame(), true, "Editar Película");
            ControladorModalPeliculas cmp = new ControladorModalPeliculas(this, mp, peliculaSelect);
            mp.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (peliculaSelect == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Seleccione una película para eliminar", DesktopNotify.ERROR, 3000);
                return;
            }

            int op = javax.swing.JOptionPane.showConfirmDialog(
                    mantto,
                    "¿Seguro que quiere eliminar permanentemente la película \"" + peliculaSelect.getTitulo() + "\"? ",
                    "Confirmar Eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            if (op == javax.swing.JOptionPane.YES_OPTION) {

                if (daoPelicula.delete(peliculaSelect)) {  // ← cambio
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Película eliminada", DesktopNotify.SUCCESS, 3000);
                    mostrar(daoPelicula.selectAll()); // ← cambio
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar la película", DesktopNotify.ERROR, 3000);
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimpleCircular<Pelicula> lista;

                if (textoBusqueda.isEmpty()) {
                    lista = daoPelicula.selectAll(); // ← cambio
                } else {
                    lista = daoPelicula.buscar(textoBusqueda);
                }

                mostrar(lista);
            }
        });
    }

    public void mostrar(ListaSimpleCircular<Pelicula> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "TÍTULO", "DURACIÓN MINUTOS", "GÉNERO", "CLASIFICACIÓN"};
        modelo.setColumnIdentifiers(titulos);

        ArrayList<Pelicula> listaPeliculas = lista.toArray();
        for (Pelicula p : listaPeliculas) {
            Object[] fila = {
                p.getId_pelicula(),
                p.getTitulo(),
                p.getDuracion_minutos(),
                p.getGenero(),
                p.getClasificacion()
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        mantto.tbDatos.setRowSorter(sorter);
        mantto.tbDatos.setModel(modelo);

        int[] anchosFijos = {30, 250, 50, 100, 100};
        ajustarAnchoColumnas(anchosFijos);
    }

    private void onClickTabla() {
        mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowVista = mantto.tbDatos.getSelectedRow();
                if (rowVista >= 0) {
                    int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                    Object idValue = mantto.tbDatos.getModel().getValueAt(rowModelo, 0);
                    int idPelicula = (Integer) idValue;

                    peliculaSelect = null;
                    for (Pelicula p : listaActualMostrada.toArray()) {
                        if (p.getId_pelicula() == idPelicula) {
                            peliculaSelect = p;
                            break;
                        }
                    }

                    boolean seleccionado = (peliculaSelect != null);
                    mantto.btnEditar.setEnabled(seleccionado);
                    mantto.btnEliminar.setEnabled(seleccionado);
                    mantto.btnAgregar.setEnabled(!seleccionado);
                }
            }
        });
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}
