package knu.intra.harvestor;

import knu.intra.harvestor.module.Harvestor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component(value = "HarvestComponent")
public class HarvestComponent {
    private @Value("${spring.kafka.broker}") String kafkaBroker;
    private @Value("${spring.kafka.topic.subscriber}") String subTopic;
    private @Value("${spring.kafka.topic.metadata}") String metaTopic;
    private @Value("${spring.kafka.topic.destination}") String dstTopic;

    private HashMap<String, Harvestor> harvestorList;

    public HarvestComponent() {
        this.harvestorList = new HashMap<>();
    }

    public void startHarvestor(String id, JSONObject parameter) throws MqttException {
        JSONObject addParam = parameter;
        addParam.put("kafkaBroker", kafkaBroker);
        addParam.put("subTopic", subTopic);
        addParam.put("metaTopic", metaTopic);
        addParam.put("dstTopic", dstTopic);

        harvestorList.put(id, new Harvestor(addParam));
        Harvestor harvestor = (Harvestor) harvestorList.get(id);
        harvestor.start();
    }

    public void stopHarvestor(String id) throws MqttException {
        Harvestor harvestor = (Harvestor) harvestorList.remove(id);
        harvestor.stop();
    }
}
