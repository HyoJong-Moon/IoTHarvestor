package knu.intra.harvestor.module;

import knu.intra.harvestor.kafka.Consumer;
import knu.intra.harvestor.kafka.Producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;

/**
 * 일정시간 동안 발생하는 IoT 데이터의 새로운 메타데이터 추출
 * 현재는 메타데이터에 대해 정해진 것이 없어 개발하지 않은 상태
 */
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
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.getKafkaConsumer().poll(Duration.ofSeconds(1000));
                for (ConsumerRecord<String, String> record : records) {
                    producer.produce(record.value());
                }
            }
        } catch (Exception e) {
            // TODO: handle finally clause
        } finally {

        }
    }
}
