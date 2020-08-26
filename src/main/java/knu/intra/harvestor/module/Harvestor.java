package knu.intra.harvestor.module;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

/**
 * IoT 하베스터 객체
 * MQTT Subscriber, 메타데이터 추출, 데이터 저장하는 객체 한번에 실행
 */
public class Harvestor {
    private String harvestId;
    private MqttSubscriber mqttSubscriber;
    private Thread dataSaverThread;

    public Harvestor(JSONObject parameter) {
        this.harvestId = parameter.getString("harvestId");
        String resourceId = parameter.getString("resourceId");
        String distributionId = parameter.getString("distributionId");
        String userId = parameter.getString("userId");
        String mqttBoker = parameter.getString("mqttBroker");
        String mqttTopic = parameter.getString("mqttTopic");
        String clientId = parameter.getString("clientId");
        String kafkaBroker = parameter.getString("kafkaBroker");
        String subTopic = parameter.getString("subTopic");
        String metaTopic = parameter.getString("metaTopic");
        String dstTopic = parameter.getString("dstTopic");

        this.mqttSubscriber = new MqttSubscriber(harvestId, mqttBoker, mqttTopic, clientId).setKafkaProducer(kafkaBroker, subTopic);
        this.dataSaverThread = new Thread(new DataSaver(harvestId, kafkaBroker, metaTopic, dstTopic, resourceId, distributionId, userId));
    }

    public void start() throws MqttException {
        mqttSubscriber.connect();
        dataSaverThread.start();
    }

    public void stop() throws MqttException {
        mqttSubscriber.disConnect();
        dataSaverThread.interrupt();
    }

    public void delete() throws MqttException {
        mqttSubscriber.terminate();
    }
}
