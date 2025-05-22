
package com.pas.saludbucalcentralbackend;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pas.saludbucalcentralbackend.database.dbConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiServer {
    private static final int port = 8080;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public static void httpServer(){
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                clientSocket = serverSocket.accept();
                new Thread(() -> handleClientRequest(clientSocket)).start();
                System.out.println("Iniciado back");
            }
        } catch (IOException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void handleClientRequest(Socket clientSocket) {
	    System.out.println("HTTP: Handle Client: " + clientSocket.toString());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // Leer la solicitud HTTP
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            // Parsear método y ruta
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Leer headers
            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                }
            }
            // Leer body si existe
            String body = null;
            if (headers.containsKey("Content-Length")) {
                int len = Integer.parseInt(headers.get("Content-Length"));
                byte[] buf = new byte[len];
                for (int i = 0; i < len;) {
	                byte[] bytes = Character.toString((char)in.read()).getBytes("utf-8");
	                for (int j = 0; j < bytes.length; j++)
		                buf[i + j] = bytes[j];
		            i += bytes.length;
                }
                body = new String(buf, "utf-8");
            }
            // Procesar la solicitud
            String response = processRequest(method, path, body);
            
            // Enviar respuesta
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: application/json");
            out.println("Connection: close");
            out.println();
            out.println(response);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static String processRequest(String method, String path, String body) {
	    System.out.println("HTTP [" + method + "] " + path + " = '" + body + "'");
        try {
            if (path.equals("/login") && method.equals("POST")) {
                return dbConnection.login(body);
            } else if (path.equals("/register") && method.equals("POST")) {
                return dbConnection.registerUser(body);
            } else if (path.equals("/syncUsers") && method.equals("GET")) {
                return dbConnection.syncUsersMobile();
            } else if (path.equals("/recieverData") && method.equals("POST")) {
	            System.out.println("Test");
                return dbConnection.recieverDataMobile(body);
            } else if (path.equals("/exportCSV") && method.equals("GET")) {
                return dbConnection.exportArchiveCSV();
            } else {
                return new JSONObject().put("error", "Ruta no encontrada").toString();
            }
        } catch (Exception e) {
            return new JSONObject().put("error", e.getMessage()).toString();
        }
    }
    
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = messageDigest.digest(input.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashBytes)
                stringBuilder.append(String.format("%02x", b));
        return stringBuilder.toString();
    }

    
    
    /*public static String JsonInsert(String tableName, JSONObject json) throws Exception{
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Iterator<String> keys = json.keys();
        
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);

            // Agregamos separador si no es el primer elemento
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }

            // Agregamos el nombre de la columna
            columns.append(key);

            // Agregamos el valor con el formato adecuado
            if (value instanceof String) {
                // Escapamos comillas simples para SQL y añadimos comillas
                String escapedValue = ((String) value).replace("'", "''");
                values.append("'").append(escapedValue).append("'");
            } else if (value instanceof Number) {
                values.append(value);
            } else if (value instanceof Boolean) {
                values.append((Boolean) value ? 1 : 0);
            } else if (value == JSONObject.NULL) {
                values.append("NULL");
            } else {
                // Para otros tipos, los convertimos a string
                values.append("'").append(value.toString()).append("'");
            }
        }

        // Construimos la consulta SQL final
        return String.format("INSERT INTO %s (%s) VALUES (%s);", 
                            tableName, columns.toString(), values.toString());
        
    }
    */
    
    
}
