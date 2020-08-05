package knu.intra.harvestor.module;

import knu.intra.harvestor.module.DataSaver;
import knu.intra.harvestor.module.MetaExtractor;
import knu.intra.harvestor.module.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

public class Harvestor {
    private String id;
    private MqttSubscriber mqttSubscriber;
    private Thread metaExtractorThread;
    private Thread dataSaverThread;

    public Harvestor(JSONObject parameter) {
        this.id = parameter.getString("id");
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

        this.mqttSubscriber = new MqttSubscriber(id, mqttBoker, mqttTopic, clientId).setKafkaProducer(kafkaBroker, subTopic);
        this.metaExtractorThread = new Thread(new MetaExtractor(id, kafkaBroker, subTopic, metaTopic));
        this.dataSaverThread = new Thread(new DataSaver(id, kafkaBroker, subTopic, dstTopic, resourceId, distributionId, userId));
    }

    public void start() throws MqttException {
        mqttSubscriber.connect();
        metaExtractorThread.start();
        dataSaverThread.start();
    }

    public void stop() throws MqttException {
        mqttSubscriber.disConnect();
        metaExtractorThread.interrupt();
        dataSaverThread.interrupt();
    }
}
