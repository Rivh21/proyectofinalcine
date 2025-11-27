/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.dao.ProductoDao;
import com.ues.edu.vista.ModalProducto;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
/**
 *
 * @author radon
 */
public class ControladorModalProducto {

    private final ControladorProducto controladorProducto;
    private final ModalProducto mp;
    private final ProductoDao dao;
    private Producto productoSelect;

    public ControladorModalProducto(ControladorProducto controladorProducto, ModalProducto mp) {
        this(controladorProducto, mp, null);
    }

    public ControladorModalProducto(ControladorProducto controladorProducto, ModalProducto mp, Producto productoSelect) {
        this.controladorProducto = controladorProducto;
        this.mp = mp;
        this.dao = controladorProducto.getDaoProducto(); 
        this.productoSelect = productoSelect;

        if (productoSelect != null) cargarDatos();
        onClickGuardar();
    }

    private void cargarDatos() {
        mp.tfNombre.setText(productoSelect.getNombre());
        mp.tfPrecioVenta.setText(String.valueOf(productoSelect.getPrecioVenta()));
    }

    private void onClickGuardar() {
        mp.btnGuardar.addActionListener(e -> {
            String nombre = mp.tfNombre.getText().trim();
            String precioStr = mp.tfPrecioVenta.getText().trim();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "Debe llenar todos los campos", DesktopNotify.ERROR, 3000);
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException ex) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Error", "El precio debe ser numérico", DesktopNotify.ERROR, 3000);
                return;
            }

            boolean exito;

            if (productoSelect == null) {
                Producto nuevo = new Producto(0, nombre, precio);
                exito = dao.insert(nuevo);

                if (exito) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Producto agregado correctamente",
                            DesktopNotify.SUCCESS, 3000);
                }
            } else {
                productoSelect.setNombre(nombre);
                productoSelect.setPrecioVenta(precio);
                exito = dao.update(productoSelect);

                if (exito) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Producto actualizado correctamente",
                            DesktopNotify.SUCCESS, 3000);
                }
            }

            if (exito) {
                controladorProducto.cargarLista(); 
                mp.dispose();
            }
        });
    }
}
