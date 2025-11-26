package ng.edu.futo.sict_e_library.user_service.repository;

import ng.edu.futo.sict_e_library.user_service.entity.User;
import ng.edu.futo.sict_e_library.user_service.enums.AccountType;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("User Repository Tests")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testStudent;
    private User testStaff;
    private User testAdmin;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Create test student
        testStudent = User.builder()
                .email("student@futo.edu.ng")
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STUDENT)
                .department("Computer Science")
                .matricNumber("20201268043")
                .gradYear(2025)
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();

        // Create test staff
        testStaff = User.builder()
                .email("staff@futo.edu.ng")
                .firstName("Jane")
                .lastName("Smith")
                .role(UserRole.ACADEMIC_STAFF)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STAFF)
                .department("Computer Science")
                .staffId("89001")
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();

        // Create test admin
        testAdmin = User.builder()
                .email("admin@futo.edu.ng")
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN_STAFF)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.ADMIN)
                .department("IT Department")
                .staffId("ADMIN001")
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        // When
        User savedUser = userRepository.save(testStudent);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("student@futo.edu.ng");
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        // Given
        userRepository.save(testStudent);

        // When
        Optional<User> found = userRepository.findByEmail("student@futo.edu.ng");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("student@futo.edu.ng");
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void testFindByEmail_NotFound() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@futo.edu.ng");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should check if email exists")
    void testExistsByEmail() {
        // Given
        userRepository.save(testStudent);

        // When & Then
        assertThat(userRepository.existsByEmail("student@futo.edu.ng")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@futo.edu.ng")).isFalse();
    }

    @Test
    @DisplayName("Should check if matric number exists")
    void testExistsByMatricNumber() {
        // Given
        userRepository.save(testStudent);

        // When & Then
        assertThat(userRepository.existsByMatricNumber("20201268043")).isTrue();
        assertThat(userRepository.existsByMatricNumber("CS/2020/999")).isFalse();
    }

    @Test
    @DisplayName("Should check if staff ID exists")
    void testExistsByStaffId() {
        // Given
        userRepository.save(testStaff);

        // When & Then
        assertThat(userRepository.existsByStaffId("89001")).isTrue();
        assertThat(userRepository.existsByStaffId("STAFF999")).isFalse();
    }

    @Test
    @DisplayName("Should find users by role")
    void testFindByRole() {
        // Given
        userRepository.save(testStudent);
        userRepository.save(testStaff);
        userRepository.save(testAdmin);

        // When
        List<User> students = userRepository.findByRole(UserRole.STUDENT);
        List<User> staff = userRepository.findByRole(UserRole.ACADEMIC_STAFF);
        List<User> admins = userRepository.findByRole(UserRole.ADMIN_STAFF);

        // Then
        assertThat(students).hasSize(1);
        assertThat(staff).hasSize(1);
        assertThat(admins).hasSize(1);
    }

    @Test
    @DisplayName("Should find users by status")
    void testFindByStatus() {
        // Given
        testStudent.setStatus(UserStatus.EXPIRED);
        userRepository.save(testStudent);
        userRepository.save(testStaff);

        // When
        List<User> activeUsers = userRepository.findByStatus(UserStatus.ACTIVE);
        List<User> expiredUsers = userRepository.findByStatus(UserStatus.EXPIRED);

        // Then
        assertThat(activeUsers).hasSize(1);
        assertThat(expiredUsers).hasSize(1);
    }

    @Test
    @DisplayName("Should find expired student accounts")
    void testFindExpiredStudentAccounts() {
        // Given
        User expiredStudent1 = User.builder()
                .email("expired1@futo.edu.ng")
                .firstName("Expired")
                .lastName("One")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STUDENT)
                .matricNumber("20201268044")
                .gradYear(2023)
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();

        User expiredStudent2 = User.builder()
                .email("expired2@students.futo.edu.ng")
                .firstName("Expired")
                .lastName("Two")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STUDENT)
                .matricNumber("CS/2018/001")
                .gradYear(2022)
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();

        userRepository.save(expiredStudent1);
        userRepository.save(expiredStudent2);
        userRepository.save(testStudent); // graduation year 2024, not expired

        // When
        List<User> expiredAccounts = userRepository.findExpiredStudentAccounts(2025);

        // Then
        assertThat(expiredAccounts).hasSize(2);
        assertThat(expiredAccounts).allMatch(user -> user.getGradYear() < 2025);
    }

    @Test
    @DisplayName("Should find students nearing expiry")
    void testFindStudentsNearingExpiry() {
        // Given
        User nearingExpiryStudent = User.builder()
                .email("nearing@students.futo.edu.ng")
                .firstName("Nearing")
                .lastName("Expiry")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STUDENT)
                .matricNumber("CS/2021/001")
                .gradYear(2026)
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .expiryWarningEmailSentAt(null)
                .build();

        userRepository.save(nearingExpiryStudent);
        userRepository.save(testStudent); // graduation year 2024

        // When
        List<User> nearingExpiry = userRepository.findStudentsNearingExpiry(2026);

        // Then
        assertThat(nearingExpiry).hasSize(1);
        assertThat(nearingExpiry.getFirst().getGradYear()).isEqualTo(2026);
        assertThat(nearingExpiry.getFirst().getExpiryWarningEmailSentAt()).isNull();
    }

    @Test
    @DisplayName("Should count users by role")
    void testCountByRole() {
        // Given
        userRepository.save(testStudent);
        userRepository.save(testStaff);
        userRepository.save(testAdmin);

        // When
        Long studentCount = userRepository.countByRole(UserRole.STUDENT);
        Long staffCount = userRepository.countByRole(UserRole.ACADEMIC_STAFF);
        Long adminCount = userRepository.countByRole(UserRole.ADMIN_STAFF);

        // Then
        assertThat(studentCount).isEqualTo(1);
        assertThat(staffCount).isEqualTo(1);
        assertThat(adminCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should count users by status")
    void testCountByStatus() {
        // Given
        testStudent.setStatus(UserStatus.EXPIRED);
        userRepository.save(testStudent);
        userRepository.save(testStaff);

        // When
        Long activeCount = userRepository.countByStatus(UserStatus.ACTIVE);
        Long expiredCount = userRepository.countByStatus(UserStatus.EXPIRED);

        // Then
        assertThat(activeCount).isEqualTo(1);
        assertThat(expiredCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find users by department")
    void testFindByDepartment() {
        // Given
        userRepository.save(testStudent);
        userRepository.save(testStaff);

        User mathStudent = User.builder()
                .email("math@students.futo.edu.ng")
                .firstName("Math")
                .lastName("Student")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .accountType(AccountType.STUDENT)
                .department("Mathematics")
                .matricNumber("MATH/2020/001")
                .gradYear(2024)
                .emailVerified(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .build();
        userRepository.save(mathStudent);

        // When
        List<User> csUsers = userRepository.findByDepartment("Computer Science");
        List<User> mathUsers = userRepository.findByDepartment("Mathematics");

        // Then
        assertThat(csUsers).hasSize(2);
        assertThat(mathUsers).hasSize(1);
    }

    @Test
    @DisplayName("Should search users by keyword")
    void testSearchUsers() {
        // Given
        userRepository.save(testStudent);
        userRepository.save(testStaff);
        userRepository.save(testAdmin);

        // When
        List<User> johnResults = userRepository.searchUsers("john");
        List<User> staffResults = userRepository.searchUsers("futo.edu.ng");
        List<User> smithResults = userRepository.searchUsers("Smith");

        // Then
        assertThat(johnResults).hasSize(1);
        assertThat(johnResults.getFirst().getFirstName()).isEqualToIgnoringCase("John");

        assertThat(staffResults).hasSize(3);
        assertThat(staffResults.getFirst().getEmail()).contains("futo.edu.ng");

        assertThat(smithResults).hasSize(1);
        assertThat(smithResults.getFirst().getLastName()).isEqualToIgnoringCase("Smith");
    }
}
