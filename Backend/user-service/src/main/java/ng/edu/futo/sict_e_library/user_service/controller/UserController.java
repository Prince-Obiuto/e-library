package ng.edu.futo.sict_e_library.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.edu.futo.sict_e_library.user_service.dto.request.ProfileUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.RoleUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.UserRegistrationRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.response.ApiResponseDTO;
import ng.edu.futo.sict_e_library.user_service.dto.response.UserResponseDTO;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import ng.edu.futo.sict_e_library.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> registerUser(@Valid @RequestBody UserRegistrationRequestDTO request) {
        log.info("Received registration request for email: {}", request.getEmail());

        UserResponseDTO user = userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("User registered successfully", user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(@PathVariable String userId) {
        log.info("Fetching user with ID: {}", userId);

        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user with email: {}", email);

        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        log.info("Fetching all users");

        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsersByRole(@PathVariable UserRole role) {
        log.info("Fetching users with role: {}", role);

        List<UserResponseDTO> users = userService.getAllUsersByRole(role);
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getUsersByStatus(@PathVariable UserStatus status) {
        log.info("Fetching users with status: {}", status);

        List<UserResponseDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getUsersByDepartment(@PathVariable String department) {
        log.info("Fetching users in department: {}", department);

        List<UserResponseDTO> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> searchUsers(@RequestParam String keyword) {
        log.info("Searching users with keyword: {}", keyword);

        List<UserResponseDTO> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Search completed successfully", users));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateProfile(@PathVariable String userId, @Valid @RequestBody ProfileUpdateRequestDTO request) {
        log.info("Updating profile for user ID: {}", userId);

        UserResponseDTO user = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Profile updated successfully", user));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserRole(@PathVariable String userId, @Valid @RequestBody RoleUpdateRequestDTO request) {
        log.info("Updating role for user ID: {} to {}", userId, request.getNewRole());

        UserResponseDTO user = userService.updateUserRole(userId, request);
        return ResponseEntity.ok(ApiResponseDTO.success("User role updated successfully", user));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserStatus(@PathVariable String userId, @RequestParam UserStatus status) {
        log.info("Updating status for user ID: {} to {}", userId, status);

        UserResponseDTO user = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponseDTO.success("User status updated successfully", user));
    }

    @PutMapping("/{userId}/last-login")
    public ResponseEntity<ApiResponseDTO<Void>> updateLastLogin(@PathVariable String userId) {
        log.info("Updating last login for user ID: {}", userId);

        userService.updateLastLogin(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("Last login updated successfully", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);

        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("User deleted successfully", null));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getUserStatistics() {
        log.info("Fetching user statistics");

        Map<String, Long> stats = userService.getUserStatistics();
        return ResponseEntity.ok(ApiResponseDTO.success("User statistics retrieved successfully", stats));
    }

    @GetMapping("/expired")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getExpiredAccounts() {
        log.info("Fetching expired student accounts");

        List<UserResponseDTO> expiredAccounts = userService.getExpiredStudentAccounts();
        return ResponseEntity.ok(ApiResponseDTO.success("Expired student accounts retrieved successfully", expiredAccounts));
    }
}
