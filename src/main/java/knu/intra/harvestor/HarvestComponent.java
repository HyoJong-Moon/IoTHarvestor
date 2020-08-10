package knu.intra.harvestor;

import knu.intra.harvestor.module.Harvestor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * IoT 하베스터를 관리하기 위한 컴포넌트
 * 여러 개의 하베스터를 실행시키고 이 들을 동작시키는 역할
 * 각각의 하베스터는 HarvestId로 구분돼며, 해시맵의 형태로 하베스터를 불러온다.
 */
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

    public void startHarvestor(String harvestId, JSONObject parameter) throws MqttException {
        JSONObject addParam = parameter;
        addParam.put("kafkaBroker", kafkaBroker);
        addParam.put("subTopic", subTopic);
        addParam.put("metaTopic", metaTopic);
        addParam.put("dstTopic", dstTopic);

        harvestorList.put(harvestId, new Harvestor(addParam));
        Harvestor harvestor = (Harvestor) harvestorList.get(harvestId);
        harvestor.start();
    }

    public void stopHarvestor(String harvestId) throws MqttException {
        Harvestor harvestor = (Harvestor) harvestorList.remove(harvestId);
        harvestor.stop();
    }
}
