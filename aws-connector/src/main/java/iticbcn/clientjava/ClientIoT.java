package iticbcn.clientjava;
import com.amazonaws.services.iot.client.AWSIotException;

public class ClientIoT {
    public static void main(String[] args) {
        ThingIoT thing = new ThingIoT();
        try {
            thing.conecta();
            thing.subscriu();
        } catch (AWSIotException e) {
            System.err.println("Error AWS IoT: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            System.exit(-1);
        }
    }
}