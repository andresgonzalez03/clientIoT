package iticbcn.clientjava;

import java.sql.*;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;

public class ThingIoT {
    private static final String FICH_CLAU_PUBLICA = "/home/this_andres/AWSJava/aws-connector/certificates/Aws-IoT-DEVICE2_PUBLIC.key";
    private static final String FICH_CLAU_PRIVADA = "/home/this_andres/AWSJava/aws-connector/certificates/Aws-IoT-DEVICE2_PRIV.key";
    private static final String FICH_CERT_DISP_IOT = "/home/this_andres/AWSJava/aws-connector/certificates/Aws-IoT-DEVICE2_DC.crt";
    private static final String ENDPOINT = "a302ucw63g5l7h-ats.iot.us-east-1.amazonaws.com";
    public static final String TOPIC = "arduino/reader/uid";
    public static final String CLIENT_ID = "pepe";
    public static final AWSIotQos TOPIC_QOS = AWSIotQos.QOS0;

    private static AWSIotMqttClient awsIotClient;

    public static void setClient(AWSIotMqttClient cli) {
        awsIotClient = cli;
    }
    public static void initClient() {
        String cliEP = ENDPOINT;
        String cliId = CLIENT_ID;

        String certFile = FICH_CERT_DISP_IOT;
        String pKFile = FICH_CLAU_PRIVADA;

        if (awsIotClient == null && certFile != null && pKFile != null) {
            String algorithm = null;
            KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certFile, pKFile, algorithm);
            awsIotClient = new AWSIotMqttClient(cliEP, cliId, pair.keyStore, pair.keyPassword);
        }
        if (awsIotClient == null) {
            throw new IllegalArgumentException("Error als construir client amb el certificat o les credencials.");
        }
    }
    public void conecta() throws AWSIotException{
        initClient();
        awsIotClient.connect();
    }
    public void subscriu() throws AWSIotException{
        TopicIoT topic= new TopicIoT(TOPIC, TOPIC_QOS);
        awsIotClient.subscribe(topic, true);
    }
    public static void publish(String uid) throws AWSIotException {
        String key = "nombre";
        String value = isValidUid(uid) ? "1" : "0";
        String jsonMessage = String.format("{\"%s\":\"%s\"}", key, value);
        try {
            AWSIotMessage iotMessage = new AWSIotMessage(TOPIC, AWSIotQos.QOS0);
            iotMessage.setStringPayload(jsonMessage);
            awsIotClient.publish(iotMessage);
        } catch (AWSIotException e) {
            System.err.println("Error al publicar el mensaje: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static boolean isValidUid(String uid) {
        if (uid == null || uid.isEmpty()) {
            return false;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.33.8/prova", "andres", "123")) {
            String query = "SELECT COUNT(*) FROM usuarios WHERE uid = '" + uid + "'"; 
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery(query)) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }
}