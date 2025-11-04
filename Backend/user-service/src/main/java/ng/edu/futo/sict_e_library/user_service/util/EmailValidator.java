package ng.edu.futo.sict_e_library.user_service.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailValidator {
    @Getter
    @Value("${faculty.email.allowed-domains}")
    private List<String> allowedDomains;

    @Value("${faculty.email.validation-enabled}")
    private boolean validationEnabled;

    public boolean isValidFacultyEmail(String email) {
        if (!validationEnabled) {
            return true;
        }

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String domain = extractDomain(email);
        return allowedDomains.stream().anyMatch(allowedDomain -> allowedDomain.equalsIgnoreCase(domain));
    }

    public String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }
        return email.substring(email.indexOf("@") + 1);
    }
}
