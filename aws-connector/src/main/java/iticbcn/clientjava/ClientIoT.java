package iticbcn.clientjava;

import java.sql.*;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

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

                AWSIotMessage message = new AWSIotMessage("arduino/reader/uid", AWSIotQos.QOS0, "Mensaje de prueba");
                access.insertMessage(con, message);
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