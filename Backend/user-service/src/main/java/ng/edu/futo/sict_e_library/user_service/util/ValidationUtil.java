package ng.edu.futo.sict_e_library.user_service.util;

import ng.edu.futo.sict_e_library.user_service.dto.request.UserRegistrationRequestDTO;
import ng.edu.futo.sict_e_library.user_service.enums.AccountType;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationUtil {
    public List<String> validateUserRegistration(UserRegistrationRequestDTO request) {
        List<String> errors = new ArrayList<>();

        //validate student-specific fields
        if (request.getAccountType() == AccountType.STUDENT) {
            if (request.getMatricNumber() == null || request.getMatricNumber().trim().isEmpty()) {
                errors.add("Matric number is required for student accounts.");
            }
            if (request.getGradYear() == null) {
                errors.add("Graduation year is required for student accounts.");
            } else if (request.getGradYear() < Year.now().getValue()) {
                errors.add("Graduation year cannot be in the past.");
            } else if (request.getGradYear() > Year.now().getValue() + 6) {
                errors.add("Graduation year cannot be more than 6 years into the future");
            }
        }

        //validate staff-specific fields
        if (request.getAccountType() == AccountType.STAFF || request.getAccountType() == AccountType.ADMIN) {
            if (request.getStaffId() == null || request.getStaffId().trim().isEmpty()) {
                errors.add("Staff ID is required for staff/admin accounts");
            }
        }

        return errors;
    }

    public boolean isAccountExpired(Integer gradYear) {
        if (gradYear == null) {
            return false;
        }
        return gradYear < Year.now().getValue();
    }
}
