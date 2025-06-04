
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
            String[] response = processRequest(method, path, body);
            
            // Enviar respuesta
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + response[0]);
            out.println("Access-Control-Allow-Origin: *"); // Permite cualquier origen
            out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE");
            out.println("Access-Control-Allow-Headers: Content-Type");
            out.println("Connection: close");
            out.println();
            out.println(response[1]);
            
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
    private static String[] processRequest(String method, String path, String body) {
        System.out.println("HTTP [" + method + "] " + path + " = '" + body + "'");
        String jsonType = "application/json";
        String csv = "text/csv";
        try {
            if (path.equals("/login") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.login(body)};
            } else if (path.equals("/register") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.registerUser(body)};
            } else if (path.equals("/deleteUser") && method.equals("DELETE")) {
                System.out.println("Borrado DELETE");
                return new String[]{jsonType,dbConnection.deleteUser(body)};
            } else if (path.equals("/syncUsers") && method.equals("GET")) {
                return new String[]{jsonType,dbConnection.syncUsersMobile()};
            } else if (path.equals("/users") && method.equals("GET")) {
                return new String[]{jsonType,dbConnection.users()};
            }else if (path.equals("/recieverData") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.recieverDataMobile(body)};
            } else if (path.equals("/exportCSV") && method.equals("POST")) {
                return new String[]{csv,dbConnection.exportArchiveCSV(body)};
            } else {
                return new String[]{jsonType,new JSONObject().put("error", "Ruta no encontrada").toString()};
            }
        } catch (Exception e) {
            return new String[]{jsonType,new JSONObject().put("error", e.getMessage()).toString()};
        }
    }
    
    public static String sha1(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(input.getBytes());
            for (byte b : hashBytes)
                    stringBuilder.append(String.format("%02x", b));
        } catch (Exception e) {
        }
        
        return stringBuilder.toString();
    }

}
