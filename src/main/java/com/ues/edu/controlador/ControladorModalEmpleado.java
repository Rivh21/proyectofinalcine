/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.dao.EmpleadoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.utilidades.Validaciones;
import com.ues.edu.vista.ModalEmpleado;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author jorge
 */
public class ControladorModalEmpleado {

    ControladorEmpleado ce;
    ModalEmpleado me;
    Empleado empleado;
    EmpleadoDao daoEmpleado;
    Empleado empleadoSelect;
    Validaciones validador = new Validaciones();

    public ControladorModalEmpleado(ControladorEmpleado ce, ModalEmpleado me) {
        this.ce = ce;
        daoEmpleado = new EmpleadoDao();
        this.me = me;
        empleadoSelect = null;
        onClickGuardar();
        configurarDui();
        configurarTelefono();
    }

    public ControladorModalEmpleado(ControladorEmpleado ce, ModalEmpleado me, Empleado empleadoSelect) {
        this.ce = ce;
        daoEmpleado = new EmpleadoDao();
        this.me = me;
        this.empleadoSelect = empleadoSelect;
        cargarDatos();
        onClickGuardar();
        configurarDui();
        configurarTelefono();
    }

    private void onClickGuardar() {
        this.me.btnGuardar.addActionListener((e) -> {
            if (ValidarCampos()) {
                if (empleadoSelect == null) {
                    newEmpleado();
                } else if (empleadoSelect != null) {
                    editEmpleado();
                }
            }
        });
    }

    private void newEmpleado() {
        this.empleado = new Empleado(
                me.tfNombre.getText(),
                me.tfApellido.getText(),
                me.tfDui.getText(),
                me.tfEmail.getText(),
                me.tfTelefono.getText(),
                Double.valueOf(me.tfSalario.getText()));
        if (!existeEmpleado()) {
            if (daoEmpleado.insert(empleado)) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                DesktopNotify.showDesktopMessage("Ok", "Registro creado", DesktopNotify.SUCCESS, 4000);
                this.me.dispose();
                ce.mostrar(daoEmpleado.selectAll());
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Error al guardor", DesktopNotify.ERROR, 9000);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Empleado ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editEmpleado() {
        empleadoSelect.setNombre(me.tfNombre.getText());
        empleadoSelect.setApellido(me.tfApellido.getText());
        empleadoSelect.setDui(me.tfDui.getText());
        empleadoSelect.setEmail(me.tfEmail.getText());
        empleadoSelect.setTelefono(me.tfTelefono.getText());
        empleadoSelect.setSalario(Double.valueOf(me.tfSalario.getText()));

        if (daoEmpleado.update(empleadoSelect)) {
            this.me.dispose();
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("Ok", "Registro Actualizado", DesktopNotify.SUCCESS, 4000);
            ce.mostrar(daoEmpleado.selectAll());
        }
    }

    private boolean ValidarCampos() {
        if (me.tfNombre.getText().trim().isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Nombre es obligatorio", DesktopNotify.ERROR, 3000);
            me.tfNombre.requestFocus();
            return false;
        }

        if (me.tfNombre.getText().matches(".*\\d.*")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Nombre no debe contener números.", DesktopNotify.ERROR, 3000);
            me.tfNombre.requestFocus();
            return false;
        }

        if (me.tfApellido.getText().trim().isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Apellido es obligatorio", DesktopNotify.ERROR, 3000);
            me.tfApellido.requestFocus();
            return false;
        }

        if (me.tfApellido.getText().matches(".*\\d.*")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Apellido no debe contener números.", DesktopNotify.ERROR, 3000);
            me.tfApellido.requestFocus();
            return false;
        }

        String dui = me.tfDui.getText().trim();
        if (me.tfDui.getText().trim().isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo DUI es obligatorio", DesktopNotify.ERROR, 3000);
            me.tfDui.requestFocus();
            return false;
        }

        if (!dui.matches("^\\d{8}-\\d$")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El formato del DUI debe ser ########-#", DesktopNotify.ERROR, 4000);
            me.tfDui.requestFocus();
            return false;
        }

        String email = me.tfEmail.getText().trim();
        if (email.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El campo Email es obligatorio", DesktopNotify.ERROR, 3000);
            me.tfEmail.requestFocus();
            return false;
        }
        if (!validador.validarEmail(email)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El formato del Email no es válido", DesktopNotify.ERROR, 3000);
            me.tfEmail.requestFocus();
            return false;
        }

        String telefono = this.me.tfTelefono.getText().replace("-", "");
        if (telefono.length() != 8 || !telefono.matches("\\d{8}")) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El teléfono debe tener 8 dígitos (####-####)", DesktopNotify.ERROR, 3000);
            this.me.tfTelefono.requestFocus();
            return false;
        }

        try {
            double salario = Double.parseDouble(me.tfSalario.getText().trim());
            if (salario <= 0) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "El salario debe ser un número positivo", DesktopNotify.ERROR, 3000);
                me.tfSalario.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "El salario debe ser un número válido", DesktopNotify.ERROR, 3000);
            me.tfSalario.requestFocus();
            return false;
        }

        return true;
    }

    private boolean existeEmpleado() {
        ListaSimple<Empleado> lista = daoEmpleado.selectAllTo("id_empleado", empleado.getIdEmpleado() + "");
        if (!lista.isEmpty()) {
            return true;
        }
        return false;
    }

    private void cargarDatos() {
        this.me.tfNombre.setText(this.empleadoSelect.getNombre());
        this.me.tfApellido.setText(this.empleadoSelect.getApellido());
        this.me.tfDui.setText(this.empleadoSelect.getDui());
        this.me.tfEmail.setText(this.empleadoSelect.getEmail());
        this.me.tfTelefono.setText(this.empleadoSelect.getTelefono());
        this.me.tfSalario.setText(String.valueOf(this.empleadoSelect.getSalario()));
    }

    private void configurarDui() {
        ((AbstractDocument) this.me.tfDui.getDocument()).setDocumentFilter(new DocumentFilter() {
            private boolean isRestoring = false; //Para evitar recursividad

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (isRestoring) {
                    return;
                }

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (text != null && !text.matches("\\d*")) {
                    return;
                }

                String digitsOnly = newText.replace("-", "");

                if (digitsOnly.length() <= 9) {
                    if (digitsOnly.length() == 8 && !newText.contains("-")) {
                        fb.replace(offset, length, text + "-", attrs); //Agrega guion después del octavo dígito
                    } else if (digitsOnly.length() > 8) {
                        String formatted = digitsOnly.substring(0, 8) + "-" + digitsOnly.substring(8);
                        fb.replace(0, fb.getDocument().getLength(), formatted, attrs);
                    } else {
                        fb.replace(offset, length, text, attrs);
                    }
                }
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                if (isRestoring) {
                    return;
                }

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                fb.remove(offset, length);

                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()).replace("-", "");
                int guionIndex = currentText.indexOf("-");

                if (newText.length() == 8 && guionIndex == -1) {
                    fb.insertString(8, "-", null);
                } else if (newText.length() < 8 && guionIndex != -1) {
                    fb.remove(guionIndex, 1);
                }
            }
        });

        this.me.tfDui.setText("########-#"); 
    }

    private void configurarTelefono() {
        ((AbstractDocument) this.me.tfTelefono.getDocument()).setDocumentFilter(new DocumentFilter() {
            private boolean isRestoring = false; // Bandera para evitar bucles 

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (isRestoring) {
                    return;  
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (text != null && !text.matches("\\d*")) {
                    return; 
                }

             
                String digitsOnly = newText.replace("-", "");
                if (digitsOnly.length() <= 8) {
                   
                    if (digitsOnly.length() == 4 && !currentText.contains("-")) {
                        fb.replace(offset, length, text + "-", attrs); 
                    } else if (digitsOnly.length() > 4) {
                       
                        String formattedText = digitsOnly.substring(0, 4) + "-" + digitsOnly.substring(4);
                        fb.replace(0, fb.getDocument().getLength(), formattedText, attrs);
                    } else {
                        fb.replace(offset, length, text, attrs);
                    }
                }
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                if (isRestoring) {
                    return; 
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                fb.remove(offset, length);

                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()).replace("-", "");
                int guionIndex = currentText.indexOf("-");

                if (newText.length() == 4 && guionIndex == -1) {
                   
                    fb.insertString(4, "-", null);
                } else if (newText.length() < 4 && guionIndex != -1) {
                   
                    fb.remove(guionIndex, 1);
                }
            }
        });
        this.me.tfTelefono.setText("####-####"); 
    }
}
