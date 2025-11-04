package ng.edu.futo.sict_e_library.user_service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.edu.futo.sict_e_library.user_service.dto.request.ProfileUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.RoleUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.UserRegistrationRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.response.UserResponseDTO;
import ng.edu.futo.sict_e_library.user_service.entity.User;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import ng.edu.futo.sict_e_library.user_service.exception.DuplicateUserException;
import ng.edu.futo.sict_e_library.user_service.exception.InvalidEmailDomainException;
import ng.edu.futo.sict_e_library.user_service.exception.UserNotFoundException;
import ng.edu.futo.sict_e_library.user_service.repository.UserRepository;
import ng.edu.futo.sict_e_library.user_service.service.UserService;
import ng.edu.futo.sict_e_library.user_service.util.EmailValidator;
import ng.edu.futo.sict_e_library.user_service.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final ValidationUtil validationUtil;

    @Override
    public UserResponseDTO registerUser(UserRegistrationRequestDTO request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Validate email domain
        if (!emailValidator.isValidFacultyEmail(request.getEmail())) {
            throw new InvalidEmailDomainException(
                    "Email must be from an approved faculty domain: " + emailValidator.getAllowedDomains()
            );
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User with email " + request.getEmail() + " already exists");
        }

        // Check for duplicate matric number
        if (request.getMatricNumber() != null && userRepository.existsByMatricNumber(request.getMatricNumber())) {
            throw new DuplicateUserException("User with matric number " + request.getMatricNumber() + " already exists");
        }

        // Check for duplicate staff ID
        if (request.getStaffId() != null && userRepository.existsByStaffId(request.getStaffId())) {
            throw new DuplicateUserException("User with staff ID " + request.getStaffId() + " already exists");
        }

        // Validate registration data
        List<String> validationErrors = validationUtil.validateUserRegistration(request);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", validationErrors));
        }

        // Create user entity
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .accountType(request.getAccountType())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .matricNumber(request.getMatricNumber())
                .staffId(request.getStaffId())
                .gradYear(request.getGradYear())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return mapToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getAllUsersByRole(UserRole role) {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateProfile(String userId, ProfileUpdateRequestDTO request) {
        log.info("Updating profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }

        User updatedUser = userRepository.save(user);
        log.info("Profile updated successfully for user ID: {}", userId);

        return mapToResponseDTO(updatedUser);
    }

    @Override
    public UserResponseDTO updateUserRole(String userId, RoleUpdateRequestDTO request) {
        log.info("Updating role for user ID: {} to {}", userId, request.getNewRole());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setRole(request.getNewRole());
        User updatedUser = userRepository.save(user);

        log.info("Role updated successfully for user ID: {}", userId);
        return mapToResponseDTO(updatedUser);
    }

    @Override
    public UserResponseDTO updateUserStatus(String userId, UserStatus status) {
        log.info("Updating status for user ID: {} to {}", userId, status);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setStatus(status);
        User updatedUser = userRepository.save(user);

        log.info("Status updated successfully for user ID: {}", userId);
        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getUserStatistics() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("adminStaff", userRepository.countByRole(UserRole.ADMIN_STAFF));
        stats.put("academicStaff", userRepository.countByRole(UserRole.ACADEMIC_STAFF));
        stats.put("students", userRepository.countByRole(UserRole.STUDENT));
        stats.put("guests", userRepository.countByRole(UserRole.GUEST));
        stats.put("activeUsers", userRepository.countByStatus(UserStatus.ACTIVE));
        stats.put("inactiveUsers", userRepository.countByStatus(UserStatus.INACTIVE));
        stats.put("expiredUsers", userRepository.countByStatus(UserStatus.EXPIRED));
        stats.put("suspendedUsers", userRepository.countByStatus(UserStatus.SUSPENDED));

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getExpiredStudentAccounts() {
        int currentYear = Year.now().getValue();
        return userRepository.findExpiredStudentAccounts(currentYear).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateLastLogin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .accountType(user.getAccountType())
                .phoneNumber(user.getPhoneNumber())
                .department(user.getDepartment())
                .matricNumber(user.getMatricNumber())
                .staffId(user.getStaffId())
                .gradYear(user.getGradYear())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
}
