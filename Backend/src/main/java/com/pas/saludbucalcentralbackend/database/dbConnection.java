package com.pas.saludbucalcentralbackend.database;

import static com.pas.saludbucalcentralbackend.ApiServer.sha1;
import java.io.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class dbConnection {
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String HOST = "localhost";
    private static final String DATABASE = "pas_central";
    private static final String PORT = "3306";
    
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
    
    public static String login(String json){
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        String cedula = String.valueOf(new StringBuilder(dataUser.getString("cedula")));
        String password = sha1(String.valueOf(new StringBuilder(dataUser.getString("password"))));
        JSONObject response = new JSONObject();
        
        
        try {
            PreparedStatement psmt = con.prepareStatement("SELECT nombre FROM usuarios WHERE cedula=? AND password=?");
            psmt.setLong(1,Long.parseLong(cedula));
            psmt.setString(2, password);
            ResultSet rs =psmt.executeQuery();
               
            if(rs.next()){
                response.put("success",true);
            } else{
                response.put("failed",false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return response.toString();
    }
    
    public static String registerUser(String json){
        JSONObject jsonObject = new JSONObject(json);
        String[] jsonData = jsonString(jsonObject);
        int userId = 0;
        //Creacion de la conexion
        con = conectar();
        try {
            PreparedStatement psmt= con.prepareStatement("INSERT INTO usuarios (?) VALUES(?)",Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1, jsonData[0]);
            psmt.setString(2, jsonData[1]);
            psmt.executeUpdate();

            userId = getLastInsertId(psmt);

            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        JSONObject response = new JSONObject();
        response.put("success",true);
        response.put("userId", userId);
        
        return response.toString();
    }
    
    public static String syncUsersMobile(){
        con = conectar();
        JSONArray dataArray = new JSONArray();
        JSONObject response = new JSONObject();
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cedula,nombre,password,tipo FROM usuarios WHERE estado = 'Activo'");


            while (rs.next()) {
                JSONObject data = new JSONObject();
                data.put("cedula", rs.getInt("cedula"));
                data.put("nombre", rs.getString("nombre"));
                data.put("password", rs.getString("password"));
                data.put("tipo", rs.getString("tipo"));
                dataArray.put(data);
            }
            response.put("success",true);
            response.put("dataUsers",dataArray);
        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        
                
        return response.toString();
    }
    
    public static String users(){
        con = conectar();
        JSONArray dataArray = new JSONArray();
        JSONObject response = new JSONObject();
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cedula,nombre,tipo,estado FROM usuarios");

            while (rs.next()) {
                JSONObject data = new JSONObject();
                data.put("cedula", rs.getInt("cedula"));
                data.put("nombre", rs.getString("nombre"));
                data.put("estado", rs.getString("estado"));
                data.put("tipo", rs.getString("tipo"));
                dataArray.put(data);
            }
            response.put("success",true);
            response.put("data",dataArray);
        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return response.toString();
    }
    
    public static String recieverDataMobile(String body){
	    JSONArray list = new JSONArray(body);
        JSONObject response = new JSONObject();
	    System.out.println("[recieverData] Got " + list.length() + " rows.");
        try {
	        con = conectar();
		    for (int i = 0; i < list.length(); i++) {
			    JSONObject row = list.getJSONObject(i);
		        String[] jsonData = jsonString(row);
		        String sql = "INSERT INTO formularios (" + jsonData[0] + ") VALUES(" + jsonData[1] + ")";
		        System.out.println("[recieverData] Execute SQL: '" + sql + "'");
	            PreparedStatement psmt = con.prepareStatement(sql);
	            int j = 1;
	            Iterator<String> keys = row.keys();
	            while(keys.hasNext()) {
		            String key = keys.next();
		            Object obj = row.get(key);
		            if (obj instanceof Integer)
			            psmt.setInt(j, (Integer)obj);
		            if (obj instanceof String)
			            psmt.setString(j, (String)obj);
			        j++;
	            }
	            psmt.executeUpdate();
		    }
	        con.close();
        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
	    response.put("success",true);
        return response.toString();
    }
    
    public static String exportArchiveCSV(){
        con = conectar();
        JSONArray dataArray = new JSONArray();
        JSONObject response = new JSONObject();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM formularios");
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i));
                if (i < columnCount) writer.print(",");
            }
            // Crear archivo CSV temporal
            File csvFile = new File("Datos_de_formulario.csv");
            //File csvFile = File.createTempFile("export-", ".csv");
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                // Escribir encabezados
                

                
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
        //String csvContent = new String(java.nio.file.Files.readAllBytes(csvFile.toPath()));
        
        response.put("success",true);
        response.put("data",dataArray);
        // Registrar en el historial
        } catch (Exception e) {
            
        }
        
        
        
        return response.toString();
    }
    
    private static int getLastInsertId(PreparedStatement stmt){
        ResultSet rs;
        try {
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    private static String[] jsonString(JSONObject json){
        // Extracion de las claves y los parametros de argumento a Strings
        StringBuilder keysObject = new StringBuilder();
        StringBuilder valueObject = new StringBuilder();
        Iterator<String> keys = json.keys();
        while(keys.hasNext()) {
	        String key = keys.next();
	        keysObject.append(key);
	        valueObject.append("?");
	        if (keys.hasNext()) {
		       keysObject.append(", ");
		       valueObject.append(", ");
	        }
	    }
        String[] result = {keysObject.toString(),valueObject.toString()};
        return result;
    }
}
