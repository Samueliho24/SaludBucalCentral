package com.pas.saludbucalcentralbackend;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String ruta="C:/Users/Sistemas/Documents/Proyectos/SaludBucalCentral/Frontend/login.html";
        File archiveHtml = new File(ruta);
        
        try {
            if (Desktop.isDesktopSupported() && archiveHtml.exists()) {
                URI uri = archiveHtml.toURI();
                Desktop.getDesktop().browse(uri);
            } else {
                System.out.println("No se pudo abrir el archivo HTML o no se soporta el acceso al escritorio.");
            }
            //Inicio del servidor
            System.out.println("Servidor Activo");
            ApiServer.httpServer();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar XAMPP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
}
