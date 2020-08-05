package knu.intra.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
@Builder
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;
    private String id;
    private String mqttBroker;
    private String mqttTopic;
    private String clientId;
    private String datasetId;
    private String distributionId;
    private String userId;
}
