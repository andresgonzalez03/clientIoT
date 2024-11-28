package iticbcn.clientjava;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public void insertMessage(Connection con, AWSIotMessage message) {
        String payload = message.getStringPayload();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(payload);
            String key = jsonNode.fieldNames().next();
            String idCard = jsonNode.get(key).asText();
            String queryCheck = "select user_id, user_name from usuaris where id_card = '" + idCard + "'";
            int userId = -1;
            String nom = null;
            String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            
            try(Statement st = con.createStatement(); ResultSet rs = st.executeQuery(queryCheck)) {
                if(rs.next()) {
                    userId = rs.getInt("user_id");
                    nom = rs.getString("user_name");
                }
                if(userId != -1) {
                    String queryInsert = "insert into asistencia (user_id, id_card, user_name, data) values (" + userId + ", '" + idCard + "', '" + nom + "', '" + data + "')";
                    try(Statement stInsert = con.createStatement()) {
                        int filasAfectadas = stInsert.executeUpdate(queryInsert);
                        if (filasAfectadas > 0) {
                            System.out.println("Idcard insertado correctamente en 'registres'.");
                        } else {
                            System.out.println("No se pudo insertar el registro.");
                        }
                    }
                } else {
                    System.out.println("No se encontró un usuario asociado al idCard: " + idCard);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}