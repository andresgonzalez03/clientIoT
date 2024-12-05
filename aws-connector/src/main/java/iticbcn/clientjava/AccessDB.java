package iticbcn.clientjava;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccessDB {
    public void selectAlumnes(Connection conn) {
        String query = "select * from usuaris";
        try(Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            if(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("ID: " + id +"\n" + "Name: " + name);
            }
        } catch(SQLException e) {
            System.out.println("Error al realizar la consulta: " + e.getMessage());
        }
    } 
    public static boolean insertMessage(String uid) {
        String queryCheck = "SELECT user_id, user_name FROM usuaris WHERE uid = '" + uid + "'"; 
        int userId = -1;
        String userName = null;
        boolean success = false;
        try (Connection con = ConnDB.getConnection();  
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryCheck);
            if (rs.next()) {
                userId = rs.getInt("user_id");
                userName = rs.getString("user_name");
            }
            if (userId != -1) {
                String queryInsert = "INSERT INTO asistencia (user_id, id_card, user_name, data) VALUES (" + userId + ", '" + uid + "', '" + userName + "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "')";
                int filasAfectadas = stmt.executeUpdate(queryInsert);
                if (filasAfectadas > 0) {
                    System.out.println("ID Card insertado correctamente.");
                    success = true;
                } else {
                    System.out.println("No se pudo insertar el registro.");
                }
            } else {
                System.out.println("No se encontró un usuario asociado al UID: " + uid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        
        return success;
    }    
}