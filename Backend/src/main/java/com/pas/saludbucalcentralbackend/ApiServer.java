
package com.pas.saludbucalcentralbackend;

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pas.saludbucalcentralbackend.database.dbConnection;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiServer {
    private static final long SECRET_KEY = 0xA23A23D;
    
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
            //Inicio de Seccion
            if (path.equals("/login") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.login(body)};
                
            //Registrar usuario nuevo
            } else if (path.equals("/register") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.registerUser(body)};
                
            //Cambiar contraseña
            }else if (path.equals("/changePassword") && method.equals("POST")) {
                return new String[]{jsonType,dbConnection.changePassword(body)};
                
            //Borrado y desactivacion de usuario
            } else if (path.equals("/deleteUser") && method.equals("DELETE")) {
                System.out.println("Borrado DELETE");
                return new String[]{jsonType,dbConnection.deleteUser(body)};
                
            //Sincronizacion de usuarios
            } else if (path.equals("/syncUsers") && method.equals("GET")) {
                return new String[]{jsonType,dbConnection.syncUsersMobile()};
                
            //Obtener lista de usuarios
            } else if (path.equals("/users") && method.equals("GET")) {
                return new String[]{jsonType,dbConnection.users()};
                
            //Obtener codigo Ip
            } else if (path.equals("/ipKey") && method.equals("GET")) {
                return new String[]{jsonType,ipKey()};
                
            //Recibir datos de los dispositivos
            }else if (path.equals("/recieverData") && method.equals("POST")) {
                return new String[]{jsonType,confirmUser(body)};
            
            //Exportar CSV
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
    
    private static String confirmUser(String body){
        JSONObject bodyObject = new JSONObject(body);
        JSONObject dataUser = new JSONObject();
        JSONObject response = new JSONObject();
        
        dataUser.put("cedula", String.valueOf(new StringBuilder(dataUser.getString("examinador_cedula"))));
        dataUser.put("password", String.valueOf(new StringBuilder(dataUser.getString("password"))));
        JSONObject user=new JSONObject(dbConnection.login(dataUser.toString()));
        try{
            if(user.getBoolean("success")){
                JSONArray data = new JSONArray(body);
                for (int i=0;i<data.length();i++){
                    data.getJSONObject(i).remove("password");
                }
                //Prueba consola
                System.out.println(data.toString());
                //
                response=new JSONObject(dbConnection.recieverDataMobile(data.toString()));
            }else if(user.getBoolean("failed")){
                response.put("failed", "El usuario que envio los datos no se encuentra registrado en este equipo");
            }
        }catch(Exception e){
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE,null,e);
            response.put("failed", "El usuario que envio los datos no pudo ser validado");
        }
        return response.toString();
    }
    
    private static String ipKey(){
        JSONObject response = new JSONObject();
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            
            response.put("success",true);
            response.put("key", encryptIP(ip));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
            response.put("failed", false);
        }
        return response.toString();
    }
    
    //Cifrado 
    private static String encryptIP(String ip){
        String[] oc = ip.split("\\.");
        //Convertir arreglo en long
        long ipInt = (Long.parseLong(oc[0])<<24)+(Long.parseLong(oc[1])<<16)+(Long.parseLong(oc[2])<<8)+(Long.parseLong(oc[3]));
        //Convertir Long en String
        String encrypt = Long.toString(ipInt^SECRET_KEY,36);
        
        String code = String.format("%8s",encrypt).replace(' ', '0');
        return code;
    }
    
    //Descifrado
    private static String decryptIP(String code){
        //Convertir codigo de String a Long
        long encrypt = Long.parseLong(code,36);
        long ipInt = encrypt^SECRET_KEY;
        //Convertir ip Long en String
        String ip =((ipInt >> 24) & 0xFF) + "." + ((ipInt >> 16) & 0xFF) + "." + ((ipInt >> 8) & 0xFF) + "." + (ipInt & 0xFF);
        
        return ip;
    }
    
}
