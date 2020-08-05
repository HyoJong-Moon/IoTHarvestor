package knu.intra.harvestor.module;

import knu.intra.harvestor.kafka.Producer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class MqttSubscriber implements MqttCallback {
    private Log log = LogFactory.getLog(MqttSubscriber.class);
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private MemoryPersistence memoryPersistence;
    private Producer producer;

    private String id;
    private String mqttBroker;
    private String mqttTopic;
    private String clientId;

    public MqttSubscriber(String id, String mqttBroker, String mqttTopic, String clientId) {
        this.id = id;
        this.mqttBroker = mqttBroker;
        this.mqttTopic = mqttTopic;
        this.clientId = clientId;
    }

    public MqttSubscriber setKafkaProducer(String kafkaBroker, String prodTopic) {
        producer = new Producer(kafkaBroker, prodTopic);
        return this;
    }

    public void connect() throws MqttException {
        memoryPersistence = new MemoryPersistence();
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttClient = new MqttClient(mqttBroker, clientId, memoryPersistence);
        mqttClient.setCallback(this);
        mqttClient.connect(mqttConnectOptions);

        log.info("Subscribe MQTT Topic: " + mqttTopic);
        mqttClient.subscribe(mqttTopic, 0);
        log.info("Subscription Succeed");
    }

    public void disConnect() throws MqttException {
        log.info("Disconnect MQTT Broker: " + mqttClient.getServerURI());
        mqttClient.disconnect();
        log.info("Succeed");
    }

    public void terminate() throws MqttException {
        mqttClient.close();
        producer.terminate();
    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject iotData;
        String message = new String(mqttMessage.getPayload());
        iotData = new JSONObject(message);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("data", iotData);
        producer.produce(String.valueOf(jsonObject));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
