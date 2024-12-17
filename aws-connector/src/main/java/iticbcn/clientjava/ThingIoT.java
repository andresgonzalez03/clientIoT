package iticbcn.clientjava;


import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;

public class ThingIoT {
    private static final String FICH_CLAU_PUBLICA = "aws-connector/certificates/Aws-IoT-DEVICE2_PUBLIC.key";
    private static final String FICH_CLAU_PRIVADA = "aws-connector/certificates/Aws-IoT-DEVICE2_PRIV.key";
    private static final String FICH_CERT_DISP_IOT = "aws-connector/certificates/Aws-IoT-DEVICE2_DC.crt";
    private static final String ENDPOINT = "a302ucw63g5l7h-ats.iot.us-east-1.amazonaws.com";
    public static final String TOPIC_UID = "esp32/sub";
    public static final String TOPIC_RESPONSE = "arduino/reader/uid";
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
            throw new IllegalArgumentException("Error al construir client con el certificado o las credenciales.");
        }
    }
    public void conecta() throws AWSIotException{
        initClient();
        awsIotClient.connect();
    }
    public void subscriu() throws AWSIotException{
        TopicIoT topic= new TopicIoT(TOPIC_UID, TOPIC_QOS);
        awsIotClient.subscribe(topic, true);
    }
    public static void publish(String value) throws AWSIotException {
        String key = "status";
        String jsonMessage = String.format("{\"%s\":\"%s\"}", key, value);
        try {
            AWSIotMessage iotMessage = new AWSIotMessage(TOPIC_RESPONSE, AWSIotQos.QOS0);
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
}