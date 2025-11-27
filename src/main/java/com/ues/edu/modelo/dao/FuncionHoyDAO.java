/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.modelo.dao;
import com.ues.edu.modelo.FuncionHoy;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.sql.*;

/**
 *
 * @author radon
 */
public class FuncionHoyDAO {
    
    Conexion conectar;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    private final String SQL_SELECT_HOY = 
        "SELECT F.id_funcion, TIME_FORMAT(F.fecha_hora_inicio, '%H:%i') AS Hora_Inicio, P.titulo "
            + "AS Pelicula, S.nombre_sala AS Sala, CASE WHEN NOW() < F.fecha_hora_inicio "
            + "THEN 'POR INICIAR' WHEN NOW() BETWEEN F.fecha_hora_inicio AND DATE_ADD"
            + "(F.fecha_hora_inicio, INTERVAL P.duracion_minutos MINUTE) THEN 'EN CURSO' "
            + "ELSE 'TERMINADA' END AS Estado_Funcion_Detallado FROM funciones F JOIN peliculas "
            + "P ON F.id_pelicula = P.id_pelicula JOIN salas S ON F.id_sala = S.id_sala WHERE DATE(F.fecha_hora_inicio)"
            + " = CURDATE() ORDER BY F.fecha_hora_inicio ASC";

    public FuncionHoyDAO() {
        conectar = new Conexion(); 
    }

    public ListaSimpleCircular<FuncionHoy> selectFuncionesDeHoy() {
        ListaSimpleCircular<FuncionHoy> lista = new ListaSimpleCircular<>();
        
        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(SQL_SELECT_HOY);
            rs = ps.executeQuery();

            while (rs.next()) {
                FuncionHoy fh = new FuncionHoy();
                
                fh.setIdFuncion(rs.getInt("id_funcion")); 
                fh.setHoraInicio(rs.getString("Hora_Inicio")); 
                fh.setTituloPelicula(rs.getString("Pelicula"));
                fh.setNombreSala(rs.getString("Sala"));
                fh.setEstado(rs.getString("Estado_Funcion_Detallado"));
                
                lista.insertar(fh); 
                
            }
        } catch (SQLException e) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Error al cargar la cartelera de hoy", DesktopNotify.ERROR, 3000);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                conectar.closeConexion(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }
}