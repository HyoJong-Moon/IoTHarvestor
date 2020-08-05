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
    private Long idx;
    private String harvestId;
    private String mqttBroker;
    private String mqttTopic;
    private String clientId;
    private String resourceId;
    private String distributionId;
    private String userId;
    private String harvName;
    private String createTime;
    private String startTime;
    private String endTime;
    private boolean state;

    @Builder
    public Harv(String harvestId, String mqttBroker, String mqttTopic, String clientId,
                String resourceId, String distributionId, String userId, String harvName,
                String createTime, String startTime, String endTime, boolean state) {
        this.harvestId = harvestId;
        this.mqttBroker = mqttBroker;
        this.mqttTopic = mqttTopic;
        this.clientId = clientId;
        this.resourceId = resourceId;
        this.distributionId = distributionId;
        this.userId = userId;
        this.harvName = harvName;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }
}
