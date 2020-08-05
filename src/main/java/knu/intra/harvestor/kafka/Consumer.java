package knu.intra.harvestor.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class Consumer {
    private KafkaConsumer<String, String> kafkaConsumer;

    public Consumer(String brokers, String topic) {
        this.kafkaConsumer = new KafkaConsumer<String, String>(setConfig(brokers));
        this.kafkaConsumer.subscribe(Arrays.asList(topic));
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
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
