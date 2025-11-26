package ng.edu.futo.sict_e_library.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ng.edu.futo.sict_e_library.user_service.enums.AccountType;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number ")
    private String phoneNumber;

    private String department;

    private String matricNumber;

    private String staffId;

    private Integer gradYear;
}
