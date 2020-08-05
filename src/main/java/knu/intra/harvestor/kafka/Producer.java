package knu.intra.harvestor.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

public class Producer {
    private Logger log = LoggerFactory.getLogger("IoT Producer");
    private String topic;
    private KafkaProducer<String, String> kafkaProducer;

    public Producer(String brokers, String topic) {
        this.topic = topic;
        kafkaProducer = new KafkaProducer<String, String>(setConfig(brokers));
    }

    public void produce(String data) {
        kafkaProducer.send(new ProducerRecord<String, String>(topic, data));
    }

    public void terminate() {
        log.info("Disconnect Kafka producer: " + topic);
        kafkaProducer.close();
    }

    public Properties setConfig(String brokers) {
        Properties consumerProp = new Properties();
        consumerProp.put("bootstrap.servers", brokers);
        consumerProp.put("group.id", UUID.randomUUID().toString());
        consumerProp.put("enable.auto.commit", "true");
        consumerProp.put("auto.commit.interval.ms", "1000");
        consumerProp.put("session.timeout.ms", "30000");
        consumerProp.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProp.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return consumerProp;
    }
}