package ng.edu.futo.sict_e_library.user_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpdateRequestDTO {
    @NotNull(message = "New Role is required")
    private UserRole newRole;
}
