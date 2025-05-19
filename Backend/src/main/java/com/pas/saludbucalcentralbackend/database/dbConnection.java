package com.pas.saludbucalcentralbackend.database;

import java.io.*;

import java.sql.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class dbConnection {
    private static final String USER = "root";
    private static final String PASSWORD = "241001";
    private static final String HOST = "localhost";
    private static final String DATABASE = "test";
    private static final String PORT = "3006";
    
    private static final String URL = "jdbc:mysql://"+HOST+":"+PORT+"/"+DATABASE;
    
    private static Connection con;
            
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
    
    public static String login(String json) throws SQLException{
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        String cedula = String.valueOf(new StringBuilder(dataUser.getString("cedula")));
        String password = String.valueOf(new StringBuilder(dataUser.getString("password")));
        
        PreparedStatement psmt = con.prepareStatement("SELECT name FROM usuarios WHERE cedula=? AND password=?");
        psmt.setLong(1,Long.parseLong(cedula));
        psmt.setString(2, password);
        ResultSet rs =psmt.executeQuery();
        
        JSONObject response = new JSONObject();
        
        if(rs.next()){
            response.put("success",true);
        } else{
            response.put("failed",false);
        }
        
        
        
        return response.toString();
    }
    
    public static String registerUser(String json) throws SQLException{
        String[] jsonData = jsonString(json);
        //Creacion de la conexion
        con = conectar();
        PreparedStatement psmt= con.prepareStatement("INSERT INTO usuarios (?) VALUES(?)",Statement.RETURN_GENERATED_KEYS);
        psmt.setString(1, jsonData[0]);
        psmt.setString(2, jsonData[1]);
        psmt.executeUpdate();
        
        int userId = getLastInsertId(psmt);
        
        con.close();
        
        JSONObject response = new JSONObject();
        response.put("success",true);
        response.put("userId", userId);
        
        return response.toString();
    }
    
    public static String syncUsersMobile() throws SQLException{
        con = conectar();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT cedula,name,password,type FROM usuario WHERE state=1");
        
        JSONArray dataArray = new JSONArray();
        while (rs.next()) {
            JSONObject data = new JSONObject();
            data.put("cedula", rs.getInt("cedula"));
            data.put("name", rs.getString("name"));
            data.put("password", rs.getString("password"));
            data.put("type", rs.getString("type"));
            dataArray.put(data);
        }
        
        JSONObject response = new JSONObject();
        response.put("success",true);
        response.put("dataUsers",dataArray);
                
        return response.toString();
    }
    
    public static String receiverDataMobile(String json) throws SQLException{
        String[] jsonData = jsonString(json);
        con = conectar();
        PreparedStatement psmt= con.prepareStatement("INSERT INTO formulario (?) VALUES(?)");
        psmt.setString(1, jsonData[0]);
        psmt.setString(2, jsonData[1]);
        psmt.executeUpdate();
        
        con.close();
        JSONObject response = new JSONObject();
        response.put("success",true);
        
        return response.toString();
    }
    
    public static String exportArchiveCSV() throws FileNotFoundException, SQLException, IOException{
        con = conectar();
        // Obtener todos los datos para exportar
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM formulario");
        
        // Crear archivo CSV temporal
        File csvFile = File.createTempFile("export-", ".csv");
        try (PrintWriter writer = new PrintWriter(csvFile)) {
            // Escribir encabezados
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i));
                if (i < columnCount) writer.print(",");
            }
            writer.println();
            
            // Escribir datos
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.print(rs.getString(i));
                    if (i < columnCount) writer.print(",");
                }
                writer.println();
            }
        }
        
        // Leer archivo CSV como texto
        String csvContent = new String(java.nio.file.Files.readAllBytes(csvFile.toPath()));
        
        // Registrar en el historial
        
        JSONObject response = new JSONObject();
        response.put("success",true);
        
        return response.toString();
    }
    
    private static int getLastInsertId(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }
    
    private static String[] jsonString(String json){
        JSONObject dataUser = new JSONObject(json);
        
        //Extracion de las claves y los valores a String
        StringBuilder keysObject = new StringBuilder();
        StringBuilder valueObject = new StringBuilder();
        dataUser.keys().forEachRemaining(key -> { keysObject.append(key).append(","); valueObject.append(dataUser.get(key)).append(",");});
        
        String keys = keysObject.toString().trim().substring(0,keysObject.length()-1);
        String value = valueObject.toString().trim().substring(0, valueObject.length()-1);
        String[] result = {keys,value};
        return result;
    }
}
