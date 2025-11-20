/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo.dao;

/**
 *
 * @author radon
 */

import com.ues.edu.interfaces.IProducto;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.estructuras.Pila;
import com.ues.edu.modelo.estructuras.ArbolBB;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import java.sql.*;
import java.util.ArrayList;

public class ProductoDao implements IProducto {

    private final Conexion conectar;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    private final ArbolBB arbolProductos;

    public ProductoDao() {
        conectar = new Conexion();
        arbolProductos = new ArbolBB();
        cargarArbol();
    }

    private void cargarArbol() {
        String sql = "SELECT id_producto FROM producto";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_producto");
                if (arbolProductos.buscar(id) == null) {
                    arbolProductos.insertar(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); 
            if(ps!=null) ps.close(); conectar.closeConexion(con);} catch (SQLException e) {}
        }
    }

    @Override
    public Pila<Producto> selectAll() {
        Pila<Producto> pila = new Pila<>();
        ArrayList<Integer> ids = arbolProductos.recorridoIND(); 
        for (Integer id : ids) {
            Producto p = buscarPorId(id);
            if (p != null) pila.push(p);
        }
        return pila;
    }

    @Override
    public Pila<Producto> selectAllTo(String atributo, String condicion) {
        Pila<Producto> pila = new Pila<>();
        ArrayList<Integer> ids = arbolProductos.recorridoIND();
        for (Integer id : ids) {
            Producto p = buscarPorId(id);
            if (p != null) {
                switch (atributo.toLowerCase()) {
                    case "nombre":
                        if (p.getNombre().equalsIgnoreCase(condicion)) pila.push(p);
                        break;
                    case "id_producto":
                        if (String.valueOf(p.getIdProducto()).equals(condicion)) pila.push(p);
                        break;
                }
            }
        }
        return pila;
    }

  
    @Override
    public Producto buscar(int id) {
        if (arbolProductos.buscar(id) == null) return null;
        return buscarPorId(id);
    }

    public Producto buscarNombre(String nombre) {
        //aqui es donde recorre todos los productos del arbol
        ArrayList<Integer> ids = arbolProductos.recorridoIND();
        for (Integer id : ids) {
            Producto p = buscarPorId(id);
            if (p != null && p.getNombre().toLowerCase().startsWith(nombre.toLowerCase())) return p;
        }
        return null;
    }

    @Override
    public boolean insert(Producto obj) {
        Producto existe = buscarPorNombreExacto(obj.getNombre());
        if (existe != null) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Ya existe un producto con ese nombre", DesktopNotify.ERROR, 3000);
            return false;
        }

        String sql = "INSERT INTO producto(nombre, precio_venta) VALUES (?, ?)";
        return alterarRegistro(sql, obj, true);
    }

    @Override
    public boolean update(Producto obj) {
        //aqui verifica si el producto existe
        Producto existe = buscarPorNombreExacto(obj.getNombre());
        if (existe != null && existe.getIdProducto() != obj.getIdProducto()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Ya existe un producto con ese nombre", DesktopNotify.ERROR, 3000);
            return false;
        }

        String sql = "UPDATE producto SET nombre=?, precio_venta=? WHERE id_producto=" + obj.getIdProducto();
        return alterarRegistro(sql, obj, false);
    }

    @Override
    public boolean delete(Producto obj) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getIdProducto());
            ps.execute();

            arbolProductos.eliminar(obj.getIdProducto()); // sincronizar árbol
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(ps!=null) ps.close(); conectar.closeConexion(con);} catch (SQLException e) {}
        }
        return false;
    }

  
    private boolean alterarRegistro(String sql, Producto obj, boolean insertar) {
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, obj.getNombre());
            ps.setDouble(2, obj.getPrecioVenta());
            ps.execute();

            if (insertar) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    obj.setIdProducto(id);
                    //en esta parte lo q hace es insertar el producto completo en el arbol
                    if (arbolProductos.buscar(id) == null) arbolProductos.insertar(id);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(ps!=null) ps.close(); conectar.closeConexion(con);} catch (SQLException e) {}
        }
        return false;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT id_producto, nombre, precio_venta FROM producto WHERE id_producto=?";
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setPrecioVenta(rs.getDouble(3));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(ps!=null) ps.close(); conectar.closeConexion(con);} catch (SQLException e) {}
        }
        return null;
    }

    @Override
    public Producto buscarPorNombreExacto(String nombre) {
        ArrayList<Integer> ids = arbolProductos.recorridoIND();
        for (Integer id : ids) {
            Producto p = buscarPorId(id);
            if (p != null && p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }
}

