package iticbcn.clientjava;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccessDB {
    public void selectAlumnes(Connection conn) {
        String query = "SELECT id, nom FROM usuaris"; 
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
        String queryCheck = "SELECT id, nom FROM usuaris WHERE id_targeta = '" + idTargeta + "'"; 
        int userId = -1;
        String userName = null;
        boolean success = false;

        try (Connection con = ConnDB.getConnection();
             Statement stmt = con.createStatement()) {
            // Ejecuta la consulta SELECT
            ResultSet rs = stmt.executeQuery(queryCheck);
            if (rs.next()) {
                userId = rs.getInt("id");
                userName = rs.getString("nom");
            }
            if (userId != -1) {
                // Consulta de inserción
                String queryInsert = "INSERT INTO registres (id_targeta, id_alumne, data_hora) VALUES ('" 
                        + idTargeta + "', " 
                        + userId + ", '" 
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "')";

                int filasAfectadas = stmt.executeUpdate(queryInsert);
                if (filasAfectadas > 0) {
                    System.out.println("Registro insertado correctamente para el usuario: " + userName);
                    success = true;
                } else {
                    System.out.println("No se pudo insertar el registro.");
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