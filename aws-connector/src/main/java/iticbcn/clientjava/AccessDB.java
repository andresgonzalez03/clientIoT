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
        int userId = -1;
        String userRole = null; 
        String queryCheck = "SELECT id, rol FROM usuaris WHERE id_targeta = '" + idTargeta + "'";
    
        try (Connection con = ConnDB.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryCheck);
            if (rs.next()) {
                userId = rs.getInt("id");
                userRole = rs.getString("rol");
            }
            if (userId != -1) {
                String queryInsert = null;
                if ("alumne".equalsIgnoreCase(userRole)) {
                    queryInsert = "INSERT INTO assistencia (alumne_id, data, estat) VALUES (" +
                            userId + ", '" +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                            "', 'present')";
                } else if ("professor".equalsIgnoreCase(userRole)) {
                    queryInsert = "INSERT INTO assistencia (professor_id, data, estat) VALUES (" +
                            userId + ", '" +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                            "', 'present')";
                }
                if (queryInsert != null) {
                    int rowsAffected = stmt.executeUpdate(queryInsert);
                    if (rowsAffected > 0) {
                        System.out.println("Asistencia registrada correctamente para " + userRole + " con ID: " + userId);
                        success = true;
                    } else {
                        System.out.println("No se pudo registrar la asistencia.");
                    }
                } else {
                    System.out.println("Rol desconocido para el usuario con ID: " + userId);
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