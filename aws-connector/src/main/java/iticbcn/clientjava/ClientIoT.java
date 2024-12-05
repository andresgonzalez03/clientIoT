package iticbcn.clientjava;

import java.sql.*;
import com.amazonaws.services.iot.client.AWSIotException;

public class ClientIoT {
    static final String url = "jdbc:postgresql://192.168.33.8/prova";
    static final String user = "andres";
    static final String password = "123";

    public static void main(String[] args) {
        ThingIoT thing = new ThingIoT();
        try {
            thing.conecta();
            thing.subscriu();
            try (Connection con = ConnDB.getConnection()) {
                AccessDB access = new AccessDB();
                access.selectAlumnes(con);
            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        } catch (AWSIotException e) {
            System.err.println("Error AWS IoT: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            System.exit(-1);
        }
    }
}