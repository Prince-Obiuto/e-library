package ng.edu.futo.sict_e_library.user_service.service;

import ng.edu.futo.sict_e_library.user_service.dto.request.ProfileUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.RoleUpdateRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.request.UserRegistrationRequestDTO;
import ng.edu.futo.sict_e_library.user_service.dto.response.UserResponseDTO;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationRequestDTO request);

    UserResponseDTO getUserById(String userId);

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    List<UserResponseDTO> getAllUsersByRole(UserRole role);

    List<UserResponseDTO> getUsersByStatus(UserStatus status);

    UserResponseDTO updateProfile(String userId, ProfileUpdateRequestDTO request);

    UserResponseDTO updateUserRole(String userId, RoleUpdateRequestDTO request);

    UserResponseDTO updateUserStatus(String userId, UserStatus status);

    void deleteUser(String userId);

    List<UserResponseDTO> searchUsers(String keyword);

    List<UserResponseDTO> getUsersByDepartment(String department);

    Map<String, Long> getUserStatistics();

    List<UserResponseDTO> getExpiredStudentAccounts();

    void updateLastLogin(String userId);
}
