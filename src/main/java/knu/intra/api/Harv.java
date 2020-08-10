package knu.intra.api;

import lombok.*;

import javax.persistence.*;

/**
 * IoT 하베스터에 필요한 정보를 담는 클래스
 * IoT 데이터 수집을 위한 MQTT 정보 및 데이터 저장 시 필요한 정보 포함
 * JPA를 사용하여 TABLE 자동 생성
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
@Data
public class Harv {
    private @Id String harvestId;           // 서버 내에서 하베스터를 식별하기 위한 ID
    private String mqttBroker;          // MQTT 프로토콜 주소
    private String mqttTopic;           // Subscribe 할 MQTT 토픽 이름
    private String clientId;            // MQTT에서 사용될 Client ID
    private String resourceId;
    private String distributionId;
    private String userId;
    private String harvName;            // 이름
    private String createTime;          // 생성 시간
    private String startTime;           // 시작 시간
    private String endTime;             // 종료 시간
    private boolean state;              // 실행 상태

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
