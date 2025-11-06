package ng.edu.futo.sict_e_library.user_service.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {

    private String eventId;
    private String eventType;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private LocalDateTime timestamp;
}
