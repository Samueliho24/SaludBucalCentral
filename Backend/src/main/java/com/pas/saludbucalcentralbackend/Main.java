package com.pas.saludbucalcentralbackend;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;

//Librerias para inicio en desarrollo
import java.net.URI;
import javax.swing.JOptionPane;

import com.pas.saludbucalcentralbackend.database.dbConnection;

public class Main{
    
    public static void main(String[] args) {
        //Comprobar que existe la base de datos y en caso de que no exista crear una
        File database = new File("Data.db");
        if(!database.exists()){
            try {
                database.createNewFile();
                dbConnection.createDB();
                
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //Inicio del programa compilado
        /*
        String htmlPath = "/web/login.html";
        
        // Crear directorio temporal para todos los recursos
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("web_resources");

        tempDir.toFile().deleteOnExit();

        // Lista de recursos a extraer (ajusta según tus necesidades)
        String[] resources = {
            htmlPath,
            "/web/index.html",
            "/web/styles.css",
            "/web/app.js",
            "/web/assets/delete-bin-line.svg",
            "/web/assets/draft-line.svg",
            "/web/assets/user-forbid-line.svg",
            "/web/assets/Logo_FacoLuz.png",
            "/web/assets/Logo_LUZ.png"
        };

        // Copiar todos los recursos al directorio temporal
        for (String resource : resources) {
            try (InputStream is = Main.class.getResourceAsStream(resource)) {
                if (is != null) {
                    Path dest = tempDir.resolve(resource.substring(resource.lastIndexOf('/') + 1));
                    Files.copy(is, dest, StandardCopyOption.REPLACE_EXISTING);
                    dest.toFile().deleteOnExit();
                }
            }
        }

        // Abrir el archivo HTML principal
        Path mainHtml = tempDir.resolve(htmlPath.substring(htmlPath.lastIndexOf('/') + 1));
        Desktop.getDesktop().browse(mainHtml.toUri());
    
            } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        //Inicio del programa para las pruebas
        String ruta="C:/Users/Sistemas01/Documents/Proyectos/SaludBucalCentral/Backend/src/main/webapp/login.html";
        File archiveHtml = new File(ruta);
        
        try {
            if (Desktop.isDesktopSupported() && archiveHtml.exists()) {
                URI uri = archiveHtml.toURI();
                Desktop.getDesktop().browse(uri);
            } else {
                System.out.println("No se pudo abrir el archivo HTML o no se soporta el acceso al escritorio.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Inicio del servidor
        System.out.println("Servidor Activo");
        ApiServer.httpServer();
    }
    
    
}
