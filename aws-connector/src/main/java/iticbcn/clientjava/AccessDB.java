package iticbcn.clientjava;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccessDB {
    public void selectAlumnes(Connection conn) {
        String query = "SELECT id, nom FROM usuari"; 
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) { 
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                System.out.println("ID: " + id + "\nName: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error al realizar la consulta: " + e.getMessage());
        }
    }

    public static boolean insertMessage(String idTargeta) {
        boolean success = false;
        String userId = null;
        String userRole = null;
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // Consulta para buscar al usuario por id_targeta
        String queryCheck = "SELECT nuid, rol FROM usuari WHERE nuid = '" + idTargeta + "'";
    
        try (Connection con = ConnDB.getConnection();
             Statement stmt = con.createStatement()) {
    
            // Ejecuta la consulta para obtener datos del usuario
            ResultSet rs = stmt.executeQuery(queryCheck);
            if (rs.next()) {
                userId = rs.getString("nuid");
                userRole = rs.getString("rol");
            }
    
            if (userId != null) {
                String queryInsert;
                
                // Determina si es un alumno o un profesor y construye la consulta correspondiente
                if ("alumne".equalsIgnoreCase(userRole)) {
                    queryInsert = "INSERT INTO assistencia (alumne_id, data, estat) VALUES ('" +
                            userId + "', '" +
                            currentTime + "', 'present')";
                } else if ("professor".equalsIgnoreCase(userRole)) {
                    queryInsert = "INSERT INTO assistencia (professor_id, data, estat) VALUES ('" +
                            userId + "', '" +
                            currentTime + "', 'present')";
                } else {
                    System.out.println("Rol desconocido para el usuario con ID: " + userId);
                    return false;
                }
                int rowsAffected = stmt.executeUpdate(queryInsert);
                if (rowsAffected > 0) {
                    System.out.println("Asistencia registrada correctamente para " + userRole + " con ID: " + userId);
                    success = true;
                } else {
                    System.out.println("No se pudo registrar la asistencia.");
                }
            } else {
                System.out.println("No se encontró un usuario asociado al ID de tarjeta: " + idTargeta);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return success;
    }    
}    