package com.pas.saludbucalcentralbackend.database;

import static com.pas.saludbucalcentralbackend.ApiServer.sha1;
import java.io.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

public class dbConnection {
    private static final String URL = "jdbc:sqlite:Data.db";
    
    private static Connection con;
    
    //Creacion de conexion con base de datos SQLite
    public static Connection conectar() {
        Connection conexion = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            
            conexion = DriverManager.getConnection(URL);
            System.out.println("Conexión exitosa a la base de datos");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        
        return conexion;
    }
    
    //Creacion de base de datos en caso de no existir
    public static void createDB(){
        con=conectar();
        String sqlUsuarios = createSQL.getUsuarios();
        String sqlFormularios = createSQL.getFormularios();

        try {
            
            Statement stmt = con.createStatement();
            //Creando tablas de la base de datos
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlFormularios);
            
            //Insertando Usuario admin
            PreparedStatement psmt = con.prepareStatement("INSERT INTO usuarios (cedula,nombre,password,tipo) VALUES (1234,'Admin','d033e22ae348aeb5660fc2140aec35850c4da997','Investigador')");
            psmt.executeUpdate();
            System.out.println("Base de datos creada con exito");
            con.close();
        } catch (Exception e) {
            System.err.println("Error al crear la base de datos: " + e.getMessage());
        }
        
    }
    
    //Inicio de Sesion al sistema
    public static String login(String json, boolean hash){
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        System.out.println(json);
        String cedula = String.valueOf(new StringBuilder(dataUser.getString("cedula")));
        String password = String.valueOf(new StringBuilder(dataUser.getString("password")));
        if(hash){
               password = sha1(password);
        }
        JSONObject response = new JSONObject();
        
        
        try {
            PreparedStatement psmt = con.prepareStatement("SELECT nombre,cedula,tipo FROM usuarios WHERE cedula=? AND password=?");
            psmt.setLong(1,Long.parseLong(cedula));
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();
            if(rs.next()){
                response.put("success",true);
                response.put("nombre",rs.getString(1));
                response.put("cedula",rs.getInt(2));
                response.put("tipo",rs.getString(3));
            } else{
                response.put("success",false);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return response.toString();
    }
    
    //Cambiar la contraseña de un usuario
    public static String changePassword(String json){
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        JSONObject response = new JSONObject();
        System.out.println(dataUser);
        try {
            PreparedStatement psmt = con.prepareStatement("UPDATE usuarios SET password=? WHERE cedula=?");
            String contrasena=sha1(dataUser.getString("password"));
            psmt.setString(1, contrasena);
            psmt.setInt(2, dataUser.getInt("cedula"));
            System.out.println(psmt);
            int rs = psmt.executeUpdate();
            if (rs>0){
                response.put("success",true);
            }
            con.close();

        } catch (Exception e) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,e);
        }
        
        return response.toString();
    }
    
    //Registrar usuario nuevo
    public static String registerUser(String json){
        JSONObject jsonObject = new JSONObject(json);
        String[] jsonData = jsonString(jsonObject);
        int userId = 0;
        JSONObject response = new JSONObject();
        
        //Creacion de la conexion
        con = conectar();
        try {
            System.out.println("[register Data User]");
            PreparedStatement psmt= con.prepareStatement("INSERT INTO usuarios (" + jsonData[0] + ") VALUES(" + jsonData[1] + ")",Statement.RETURN_GENERATED_KEYS);
            int j = 1;
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Object obj = jsonObject.get(key);
                String objString = (String) obj;
                
                if(key.equals("cedula")){
                    Integer objInteger = Integer.valueOf(objString);
                    if(cedulaConsult(objInteger)){
                        response.put("error","El usuario con la cedula ingresada ya existe. Por favor verificar los datos");
                        con.close();
                        return response.toString();
                    }
                }
                
                if(key.equals("password")){
                    objString = sha1(objString);
                }
                
                if (obj instanceof Integer)
                    psmt.setInt(j, (Integer)obj);
                if (obj instanceof String){
                    psmt.setString(j, objString);
                }
                j++;
            }
            psmt.executeUpdate();

            userId = getLastInsertId(psmt);

            con.close();
            
            response.put("success",true);
            response.put("userId", userId);
        
        
        } catch (Exception ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toString();
        
    }
    
    //Sincronizar usuarios con la aplicacion movil
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
            con.close();
        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        
                
        return response.toString();
    }
    
    //Obtener lista de usuarios registrados
    public static String users() throws SQLException{
        con = conectar();
        JSONArray dataArray = new JSONArray();
        JSONObject response = new JSONObject();
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cedula,nombre,tipo,estado FROM usuarios WHERE estado!='Eliminado'");

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
            con.close();

        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return response.toString();
    }
    
    //Borrar logico de usuarios
    public static String deleteUser(String json){
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        JSONObject response = new JSONObject();
        String query;
        
        if(dataUser.getString("accion").equals("Eliminar")){
            query="'Eliminado'";
        }else if(dataUser.getString("accion").equals("Desactivar")){
            query="'Inactivo'";
        }else{
            query="'Activo'";
        }
        
        try {
            PreparedStatement psmt = con.prepareStatement("UPDATE usuarios SET estado="+query+" WHERE cedula=?");
            psmt.setInt(1, dataUser.getInt("cedula"));
            int rs = psmt.executeUpdate();
            if (rs>0){
                response.put("success",true);
            }
            con.close();

        } catch (Exception e) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,e);
        }
        
        return response.toString();
    }
    
    //Recepcion de datos de formulario desde la aplicacion movil
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
    
    //Mostrar numero de formularios registrados en la base de datos
    public static String forms(){
        con = conectar();
        JSONObject response = new JSONObject();
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM formularios");
            if (rs.next()) {
                response.put("success",true);
                response.put("forms",rs.getInt(1));
            }
            con.close();

        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        System.out.println(response);
        return response.toString();
    }
    
    //Crear archivo CSV para exportar los datos de los formularios
    public static String exportArchiveCSV(String json){
        con = conectar();
        JSONObject dataUser = new JSONObject(json);
        JSONObject response = new JSONObject();
        String csvContent;
        try {
            PreparedStatement psmt = con.prepareStatement("SELECT * FROM formularios");
            ResultSet rs = psmt.executeQuery();
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
            csvContent = new String(java.nio.file.Files.readAllBytes(csvFile.toPath()));
            response.put("success", true);
            response.put("data", csvContent);

            // Eliminar archivo temporal
            csvFile.delete();
            con.close();
        } catch (Exception e) {
            response.put("error", e);
        }
        return response.toString();
    }
    
    //Vaciar tabla de formularios
    public static String deleteFormsDB(){
        JSONObject response = new JSONObject();
        con = conectar();
        try{
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM formularios");
            response.put("success",true);
            con.close();

        } catch (SQLException ex){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return response.toString();
    }
    
    //Extraer id del ultimo usuario registrado
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
    
    //Convertir JSONObjent en String
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
    
    //Consultar si la cedula del usuario a registrar pertenece a otro usuario en el sistema
    private static boolean cedulaConsult(int cedula){
        con = conectar();
        try {
            PreparedStatement psmt = con.prepareStatement("SELECT cedula FROM usuarios WHERE cedula=?");
            psmt.setInt(1, cedula);
            ResultSet rs = psmt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
