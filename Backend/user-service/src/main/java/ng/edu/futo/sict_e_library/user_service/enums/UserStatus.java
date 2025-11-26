package ng.edu.futo.sict_e_library.user_service.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Account is active and can access the system"),
    INACTIVE("Account is temporarily inactive"),
    EXPIRED("Account has expired"),
    SUSPENDED("Account is suspended by admin");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

}
