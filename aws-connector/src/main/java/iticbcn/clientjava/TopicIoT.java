package iticbcn.clientjava;

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
            System.out.println(System.currentTimeMillis() + ": <<< " + message.getStringPayload());
            String uid = payload.replaceAll("[{}\" ]", "").split(":")[1];
            if (uid != null && !uid.isEmpty()) {
                ThingIoT.publish(uid); 
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar el mensaje: " + e.getMessage());
        }
    }
}
