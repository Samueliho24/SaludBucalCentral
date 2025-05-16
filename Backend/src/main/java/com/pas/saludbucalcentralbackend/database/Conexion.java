package com.pas.saludbucalcentralbackend.database;

import com.mysql.cj.xdevapi.JsonArray;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/pasCentrl";
    private static final String USER = "root";
    private static final String PASSWORD = "241001";

    public static Connection conectar() {
        Connection conexion = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        
        return conexion;
    }
    
    public static void insertData(String json){
        Connection con = conectar();
        PreparedStatement psmt;
        
        
        
    }
}
