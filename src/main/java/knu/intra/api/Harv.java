package knu.intra.api;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
@Data
public class Harv {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;
    private String id;
    private String mqttBroker;
    private String mqttTopic;
    private String clientId;
    private String resourceId;
    private String distributionId;
    private String userId;
    private String harvName;

    @Builder
    public Harv(String id, String mqttBroker, String mqttTopic, String clientId, String resourceId, String distributionId, String userId, String harvName) {
        this.id = id;
        this.mqttBroker = mqttBroker;
        this.mqttTopic = mqttTopic;
        this.clientId = clientId;
        this.resourceId = resourceId;
        this.distributionId = distributionId;
        this.userId = userId;
        this.harvName = harvName;
    }
}
