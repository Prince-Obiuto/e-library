package ng.edu.futo.sict_e_library.user_service.repository;

import ng.edu.futo.sict_e_library.user_service.entity.User;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMatricNumber(String matricNumber);

    boolean existsByStaffId(String staffId);

    List<User> findByRole(UserRole role);

    List<User> findByStatus(UserStatus status);

    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    @Query("SELECT u FROM User u WHERE u.gradYear IS NOT NULL AND u.gradYear < :currentYear AND u.status = 'ACTIVE'")
    List<User> findExpiredStudentAccounts(@Param("currentYear") Integer currentYear);

    @Query("SELECT u FROM User u WHERE u.gradYear = :warningYear AND u.status = 'ACTIVE' AND u.expiryWarningEmailSentAt IS NULL")
    List<User> findStudentsNearingExpiry(@Param("warningYear") Integer warningYear);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") UserRole role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") UserStatus status);

    @Query("SELECT u FROM User u WHERE u.department = :department")
    List<User> findByDepartment(@Param("department") String department);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
}
