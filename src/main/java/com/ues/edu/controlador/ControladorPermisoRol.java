package com.ues.edu.controlador;

import com.ues.edu.modelo.dao.PermisoDao;
import com.ues.edu.modelo.dao.PermisoRolDao;
import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.PermisoRol;
import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.ModalPermisoRol;
import com.ues.edu.vista.VistaPermisoRol;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author radonay
 */
public class ControladorPermisoRol {

    private final PermisoRolDao prDao;
    private final PermisoDao pDao;

    private int idRolActual;
    private String nombreRolActual;
    private VistaPermisoRol vistaActual;

    private int idPermisoRolSeleccionado = -1;

    public ControladorPermisoRol() {
        this.prDao = new PermisoRolDao();
        this.pDao = new PermisoDao();
    }

    public void cargarPermisosEnTabla(int idRol, String nombreRol, VistaPermisoRol vista) {
        this.idRolActual = idRol;
        this.nombreRolActual = nombreRol;
        this.vistaActual = vista;

        vista.lblTitulo.setText("Rol seleccionado: " + nombreRol);
        vista.btnQuitarPermiso.setEnabled(false);

        onClickTabla();
        onClickAsignar();
        onClickRegresar();
        onClickQuitarPermiso();

        recargarTabla();
    }

    private void recargarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID_PR", "Permiso", "Activo", "ID_P"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 2) ? Boolean.class : super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        vistaActual.tbDatos.setModel(modelo);
        ListaSimple<PermisoRol> lista = prDao.selectByRol(idRolActual);

        for (PermisoRol pr : lista.toArray()) {
            modelo.addRow(new Object[]{
                pr.getIdPermisoRol(),
                pr.getPermiso().getNombrePermiso(),
                pr.getTienePermiso(),
                pr.getPermiso().getIdPermiso()
            });
        }

        TableColumnModel tcm = vistaActual.tbDatos.getColumnModel();
        if (tcm.getColumnCount() > 0) {
            tcm.getColumn(0).setMinWidth(0);
            tcm.getColumn(0).setMaxWidth(0);
            tcm.getColumn(0).setPreferredWidth(0);

            tcm.getColumn(3).setMinWidth(0);
            tcm.getColumn(3).setMaxWidth(0);
            tcm.getColumn(3).setPreferredWidth(0);
        }
    }

    private void onClickTabla() {
        vistaActual.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowVista = vistaActual.tbDatos.getSelectedRow();

                if (rowVista >= 0) {
                    int rowModelo = vistaActual.tbDatos.convertRowIndexToModel(rowVista);
                    Object idValue = vistaActual.tbDatos.getModel().getValueAt(rowModelo, 0);
                    idPermisoRolSeleccionado = (Integer) idValue;

                    vistaActual.btnQuitarPermiso.setEnabled(idPermisoRolSeleccionado > 0);
                } else {
                    idPermisoRolSeleccionado = -1;
                    vistaActual.btnQuitarPermiso.setEnabled(false);
                }
            }
        });
    }

    private void onClickQuitarPermiso() {
        vistaActual.btnQuitarPermiso.addActionListener(e -> {
            if (idPermisoRolSeleccionado == -1) {
                JOptionPane.showMessageDialog(vistaActual, "Seleccione un permiso para quitar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(vistaActual,
                    "¿Está seguro de quitar este permiso del rol?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                PermisoRol prEliminar = new PermisoRol();
                prEliminar.setIdPermisoRol(idPermisoRolSeleccionado);

                if (prDao.delete(prEliminar)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Éxito", "Permiso eliminado del rol.", DesktopNotify.SUCCESS, 3000L);
                    recargarTabla();
                    vistaActual.btnQuitarPermiso.setEnabled(false);
                    idPermisoRolSeleccionado = -1;
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "No se pudo eliminar.", DesktopNotify.FAIL, 3000L);
                }
            }
        });
    }

    private void onClickAsignar() {
        vistaActual.btnAsignar.addActionListener(e -> {
            ModalPermisoRol modal = new ModalPermisoRol(new JFrame(), true);
            modal.setTitle("Asignar Permiso");
            modal.lbRolSeleccionada.setText(nombreRolActual);

            modal.cmbPermisos.removeAllItems();
            ListaSimple<Permiso> listaPermisos = pDao.selectAllPermisos();
            var todosLosPermisos = listaPermisos.toArray();
            var asignados = prDao.selectByRol(idRolActual).toArray();

            for (Permiso p : todosLosPermisos) {
                boolean yaTiene = asignados.stream()
                        .anyMatch(pr -> pr.getPermiso().getIdPermiso() == p.getIdPermiso());

                if (!yaTiene) {
                    modal.cmbPermisos.addItem(p.getNombrePermiso());
                }
            }

            if (modal.cmbPermisos.getItemCount() == 0) {
                modal.cmbPermisos.addItem("Sin permisos disponibles");
                modal.cmbPermisos.setEnabled(false);
                modal.btnGuardar.setEnabled(false);
            }

            modal.btnGuardar.addActionListener(evt -> {
                String nombreP = (String) modal.cmbPermisos.getSelectedItem();
                if (nombreP != null && !nombreP.equals("Sin permisos disponibles")) {
                    var listaBusqueda = pDao.obtenerIdPorNombre(nombreP);
                    if (!listaBusqueda.isEmpty()) {
                        Permiso p = listaBusqueda.get(0);

                        PermisoRol nuevoPR = new PermisoRol();
                        Rol rol = new Rol();
                        rol.setIdRol(idRolActual);
                        nuevoPR.setRol(rol);
                        nuevoPR.setPermiso(p);
                        nuevoPR.setTienePermiso(true);

                        if (prDao.insert(nuevoPR)) {
                            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                            DesktopNotify.showDesktopMessage("Éxito", "Permiso asignado.", DesktopNotify.SUCCESS, 3000L);
                            modal.dispose();
                            recargarTabla();
                        } else {
                            JOptionPane.showMessageDialog(modal, "Error al asignar.");
                        }
                    }
                }
            });

            modal.setLocationRelativeTo(vistaActual);
            modal.setVisible(true);
        });
    }

    private void onClickRegresar() {
        vistaActual.btnRegresar.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) vistaActual.tbDatos.getModel();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                int idPR = (int) modelo.getValueAt(i, 0);
                boolean valorActual = (boolean) modelo.getValueAt(i, 2);

                // Actualiza el registro
                PermisoRol prObj = new PermisoRol();
                prObj.setIdPermisoRol(idPR);
                prObj.setTienePermiso(valorActual);

                prDao.update(prObj);
            }

            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(vistaActual);
            if (window != null) {
                window.dispose();
            }
        });
    }
}
