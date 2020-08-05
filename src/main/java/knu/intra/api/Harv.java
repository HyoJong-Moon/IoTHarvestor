package knu.intra.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
@Data
@Builder
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
}
