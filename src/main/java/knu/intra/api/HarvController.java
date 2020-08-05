package knu.intra.api;

import knu.intra.harvestor.HarvestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        String id = UUID.randomUUID().toString();
        harvRepository.save(new Harv().builder()
                .id(id)
                .mqttBroker(mqttBroker)
                .mqttTopic(mqttTopic)
                .clientId(clientId)
                .resourceId(resourceId)
                .distributionId(distributionId)
                .userId(userId)
                .harvName(harvName)
                .build()
        );
        result.put("id", id);
        return result;
    }

    @GetMapping("/get")
    public Harv getHarv(@RequestParam(value="id") String id) {
        return harvRepository.findById(id);
    }

    @GetMapping("/list")
    public List<Harv> getListHarv() {
        List<Harv> harvs = new ArrayList<>();
        harvRepository.findAll().forEach(harv -> harvs.add(harv));
        return harvs;
    }

    @PostMapping("/update")
    public Map putHarv(@RequestParam(value="id") String id,
                       @RequestParam(value="mqttBroker") String mqttBroker,
                       @RequestParam(value="mqttTopic") String mqttTopic,
                       @RequestParam(value="clientId") String clientId,
                       @RequestParam(value="resourceId") String resourceId,
                       @RequestParam(value="distributionId") String distributionId,
                       @RequestParam(value="userId") String userId,
                       @RequestParam(value="harvName") String harvName) {
        Map<String, Object> result = new HashMap<>();
        Harv harv = harvRepository.findById(id);
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
    public Map delHarv(@RequestParam(value="id") String id) {
        Map<String, Object> result = new HashMap<>();
        harvRepository.deleteById(id);
        result.put("result", true);
        return result;
    }
}
