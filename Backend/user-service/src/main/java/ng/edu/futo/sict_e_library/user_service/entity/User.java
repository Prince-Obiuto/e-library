package ng.edu.futo.sict_e_library.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String matricNumber;

    @Column(length = 50)
    private String staffId;

    private Integer gradYear; // for students only

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(nullable = false)
    private Boolean accountNotExpired = false;

    @Column(nullable = false)
    private Boolean accountNotLocked = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    private LocalDateTime expiryWarningMailSentAt;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
        if (emailVerified == null) {
            emailVerified = false;
        }
    }
}
