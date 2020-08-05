package knu.intra.harvestor.module;

import knu.intra.harvestor.kafka.Consumer;
import knu.intra.harvestor.kafka.Producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.json.JSONObject;

import java.util.UUID;

public class DataSaver implements Runnable {
    private String id;
    private String datasetId, distributionId, userId;
    private Consumer consumer;
    private Producer producer;

    public DataSaver(String id, String kafkaBroker, String consTopic, String prodTopic, String datasetId, String distributionId, String userId) {
        this.id = id;
        this.datasetId = datasetId;
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
                    String produceData = transformUMF(new JSONObject(record.value()));
                    producer.produce(produceData);
                }
            }
        } catch (Exception e) {
            // TODO: handle finally clause
        } finally {
            // log.info("Data Saving Stop !!");
        }
    }

    public String transformUMF(JSONObject data) {
        JSONObject bodyObject = new JSONObject();           // UMF body 부분 JSON
        bodyObject.put("resourceId", datasetId);
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
