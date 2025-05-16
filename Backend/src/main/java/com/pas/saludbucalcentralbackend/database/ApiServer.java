
package com.pas.saludbucalcentralbackend.database;

import org.json.JSONObject;
import java.util.Iterator;

public class ApiServer {
    public static String JsonInsert(String tableName, JSONObject json) throws Exception{
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
    
    
    
}
