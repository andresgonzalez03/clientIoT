package iticbcn.clientjava;

import org.json.JSONObject;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class TopicIoT  extends AWSIotTopic {
    public TopicIoT(String topic,AWSIotQos qos) {
        super(topic,qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        try {
            String payload = message.getStringPayload();
            System.out.println("Mensaje recibido: " + payload);
            System.out.println(System.currentTimeMillis() + ": <<< " + payload);
            JSONObject json = new JSONObject(payload);
            String uid = json.optString("cardUID");
            System.out.println("UID: " + uid);
            if (uid != null && !uid.isEmpty()) {
                boolean insertSuccess = AccessDB.insertMessage(uid);
                String status = insertSuccess ? "1" : "0";
                ThingIoT.publish(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar el mensaje: " + e.getMessage());
        }
    }
}
