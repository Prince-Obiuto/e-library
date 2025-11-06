package ng.edu.futo.sict_e_library.user_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void sendUserEvent(UserEvent event) {
        log.info("Sending user event: {} for user: {}", event.getEventType(), event.getEmail());

        kafkaTemplate.send(TOPIC, event.getUserId(), event).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("User event sent successfully: {}", event.getEventType());
            } else {
                log.error("Failed to send user event: {}", event.getEventType(), ex);
            }
        });
    }
}
