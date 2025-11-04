package ng.edu.futo.sict_e_library.user_service.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequestDTO {
    private String firstName;

    private String lastName;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number ")
    private String phoneNumber;

    private String department;
}
