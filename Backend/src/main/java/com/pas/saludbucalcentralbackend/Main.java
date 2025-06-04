package com.pas.saludbucalcentralbackend;

import javax.swing.JOptionPane;

public class Main {
    private static Process xamppStart;
    
    public static void main(String[] args) {
        try {
            //Inicio del Xampp
            xamppStart = Runtime.getRuntime().exec("C:\\xampp\\xampp-control.exe");
            
            //Inicio del servidor
            System.out.println("Servidor Activo");
            ApiServer.httpServer();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar XAMPP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    public static void cerrarXampp(){
        xamppStart.destroy();
    }
}
