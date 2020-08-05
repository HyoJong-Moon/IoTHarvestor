package knu.intra.harvestor.module;

import knu.intra.harvestor.kafka.Consumer;
import knu.intra.harvestor.kafka.Producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;

public class MetaExtractor implements Runnable {
    private String harvestId;
    private Consumer consumer;
    private Producer producer;

    public MetaExtractor(String harvestId, String kafkaBroker, String consTopic, String prodTopic) {
        this.harvestId = harvestId;
        this.consumer = new Consumer(kafkaBroker, consTopic);
        this.producer = new Producer(kafkaBroker, prodTopic);

    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.getKafkaConsumer().poll(Duration.ofSeconds(1000));
            for (ConsumerRecord<String, String> record : records) {
                String data = record.value();
                producer.produce(data);
            }
        }
    }
}
