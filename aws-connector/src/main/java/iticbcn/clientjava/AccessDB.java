package iticbcn.clientjava;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccessDB {
    public static boolean insertMessage(String idTargeta) {
        boolean success = false;
        String userId = null;
        String userRole = null;
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        
        String queryCheck = "SELECT nuid, rol FROM usuari WHERE nuid = '" + idTargeta + "'";
    
        try (Connection con = ConnDB.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryCheck);
            if (rs.next()) {
                userId = rs.getString("nuid");
                userRole = rs.getString("rol");
            }
    
            if (userId != null) {
                String queryInsert;

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