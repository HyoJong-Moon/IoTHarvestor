package knu.intra.harvestor.module;

import knu.intra.harvestor.kafka.Consumer;
import knu.intra.harvestor.kafka.Producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.json.JSONObject;

import java.util.UUID;

/**
 * IoT 데이터를 최종적으로 저장하기 위해 보내는 클래스
 * 현재는 UMF 형태로 우리 서버에 전송하는 형태
 * 이부분은 추가적인 작업 필요
 */
public class DataSaver implements Runnable {
    private String harvestId;
    private String resourceId, distributionId, userId;
    private Consumer consumer;
    private Producer producer;

    public DataSaver(String harvestId, String kafkaBroker, String consTopic, String prodTopic, String resourceId, String distributionId, String userId) {
        this.harvestId = harvestId;
        this.resourceId = resourceId;
        this.distributionId = distributionId;
        this.userId = userId;

        this.consumer = new Consumer(kafkaBroker, consTopic);
        this.producer = new Producer(kafkaBroker, prodTopic);
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.getKafkaConsumer().poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    JSONObject data = new JSONObject(record.value());

                    /* harvestId와 일치하는 데이터만 전송 */
                    if(data.getString("harvestId").equals(harvestId)) {
                        String produceData = transformUMF(new JSONObject(data.getString("data")));
                        producer.produce(produceData);
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle finally clause
        } finally {
            // log.info("Data Saving Stop !!");
        }
    }

    /* 카프카로부터 전송받은 IoT 데이터를 UMF로 포장 */
    public String transformUMF(JSONObject data) {
        JSONObject bodyObject = new JSONObject();           // UMF body 부분 JSON
        bodyObject.put("resourceId", resourceId);
        bodyObject.put("distributionId", distributionId);
        bodyObject.put("userId", userId);
        bodyObject.put("data", data.getString("data"));

        JSONObject umfObject = new JSONObject();            // UMF JSON
        umfObject.put("mid", String.valueOf(UUID.randomUUID()));
        umfObject.put("to", "");
        umfObject.put("from", "knu:distribution:1");
        umfObject.put("version", "UMF/1.4.6");
        umfObject.put("timestamp", System.currentTimeMillis());
        umfObject.put("body", bodyObject);

        return String.valueOf(umfObject);
    }
}
