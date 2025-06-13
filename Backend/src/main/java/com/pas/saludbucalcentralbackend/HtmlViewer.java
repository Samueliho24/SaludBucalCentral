
package com.pas.saludbucalcentralbackend;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
/**
 *
 * @author Sistemas
 */
public class HtmlViewer extends Application{
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        
        // Cargar el HTML desde los recursos del JAR
        String htmlContent = loadHtmlFromResources("/mypage.html");
        
        // Mostrar el contenido HTML
        webView.getEngine().loadContent(htmlContent);
        
        Scene scene = new Scene(webView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HTML Viewer");
        primaryStage.show();
    }
    
    private String loadHtmlFromResources(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error loading resource: " + resourcePath, e);
        }
    }
}
