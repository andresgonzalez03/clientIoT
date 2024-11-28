package iticbcn.clientjava;

import java.sql.*;

public class ClientIoT {
    static final String url = "jdbc:postgresql://192.168.33.8/prova";
    static final String user = "andres";
    static final String password = "123";

    public static void main(String[] args) {
        ThingIoT thing = new ThingIoT();
        try {
            thing.conecta();
            thing.subscriu();
            try (Connection con = ConnDB.getConnection(url, user, password)) {
                AccessDB access = new AccessDB();
                access.selectAlumnes(con);
                access.insertMessage(con, null);
            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error IOT: "+e.getLocalizedMessage());
            System.exit(-1);
        }    
    }
}


