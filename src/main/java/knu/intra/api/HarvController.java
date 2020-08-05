package knu.intra.api;

import knu.intra.harvestor.HarvestComponent;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value="/iot")
public class HarvController {
    private @Qualifier("HarvestComponent") @Autowired
    HarvestComponent harvestComponent;

    private @Autowired
    HarvRepository harvRepository;

    @PostMapping("/set")
    public Map setHarv(@RequestParam(value="mqttBroker") String mqttBroker,
                       @RequestParam(value="mqttTopic") String mqttTopic,
                       @RequestParam(value="clientId") String clientId,
                       @RequestParam(value="resourceId") String resourceId,
                       @RequestParam(value="distributionId") String distributionId,
                       @RequestParam(value="userId") String userId,
                       @RequestParam(value="harvName") String harvName) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String harvestId = UUID.randomUUID().toString();
        harvRepository.save(new Harv().builder()
                .harvestId(harvestId)
                .mqttBroker(mqttBroker)
                .mqttTopic(mqttTopic)
                .clientId(clientId)
                .resourceId(resourceId)
                .distributionId(distributionId)
                .userId(userId)
                .harvName(harvName)
                .createTime(dateFormat.format(System.currentTimeMillis()))
                .state(false)
                .build()
        );
        result.put("harvestId", harvestId);
        return result;
    }

    @GetMapping("/get")
    public Harv getHarv(@RequestParam(value="harvestId") String harvestId) {
        return harvRepository.findByHarvestId(harvestId);
    }

    @GetMapping("/list")
    public List<Harv> getListHarv() {
        List<Harv> harvs = new ArrayList<>();
        harvRepository.findAll().forEach(harv -> harvs.add(harv));
        return harvs;
    }

    @PostMapping("/update")
    public Map putHarv(@RequestParam(value="harvestId") String harvestId,
                       @RequestParam(value="mqttBroker") String mqttBroker,
                       @RequestParam(value="mqttTopic") String mqttTopic,
                       @RequestParam(value="clientId") String clientId,
                       @RequestParam(value="resourceId") String resourceId,
                       @RequestParam(value="distributionId") String distributionId,
                       @RequestParam(value="userId") String userId,
                       @RequestParam(value="harvName") String harvName) {
        Map<String, Object> result = new HashMap<>();
        Harv harv = harvRepository.findByHarvestId(harvestId);
        harv.setMqttBroker(mqttBroker);
        harv.setMqttTopic(mqttTopic);
        harv.setClientId(clientId);
        harv.setResourceId(resourceId);
        harv.setDistributionId(distributionId);
        harv.setUserId(userId);
        harv.setHarvName(harvName);
        harvRepository.save(harv);
        result.put("result", true);
        return result;
    }

    @Transactional
    @DeleteMapping("/del")
    public Map delHarv(@RequestParam(value="harvestId") String harvestId) {
        Map<String, Object> result = new HashMap<>();
        harvRepository.deleteByHarvestId(harvestId);
        result.put("result", true);
        return result;
    }

    @PostMapping("/start")
    public Map startHarv(@RequestParam(value="harvestId") String harvestId) {
        Map<String, Object> result = new HashMap<>();
        Harv harv = harvRepository.findByHarvestId(harvestId);

        // Parameter Setting
        JSONObject parameters = new JSONObject();
        parameters.put("mqttBroker", harv.getMqttBroker());
        parameters.put("mqttTopic", harv.getMqttTopic());
        parameters.put("clientId", harv.getClientId());
        parameters.put("resourceId", harv.getResourceId());
        parameters.put("distributionId", harv.getDistributionId());
        parameters.put("userId", harv.getUserId());
        parameters.put("harvestId", harv.getHarvestId());

        try {
            harvestComponent.startHarvestor(harvestId, parameters);
            result.put("result", true);
        } catch (MqttException e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    @PostMapping("/stop")
    public Map stopHarv(@RequestParam(value="harvestId") String harvestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            harvestComponent.stopHarvestor(harvestId);
            result.put("result", true);
        } catch (MqttException e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }
}
