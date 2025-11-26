package ng.edu.futo.sict_e_library.user_service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.edu.futo.sict_e_library.user_service.entity.User;
import ng.edu.futo.sict_e_library.user_service.enums.UserRole;
import ng.edu.futo.sict_e_library.user_service.enums.UserStatus;
import ng.edu.futo.sict_e_library.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentAccountExpiryScheduler {

    private final UserRepository userRepository;

    @Value("${scheduler.student-account-cleanup.enabled}")
    private Boolean cleanupEnabled;

    @Value("${scheduler.student-account-cleanup.warning-days-before-expiry}")
    private Integer warningDaysBeforeExpiry;

    /**
     * Runs daily at 2:00 AM to check for expired student accounts
     */

    @Scheduled(cron = "${scheduler.student-account-cleanup.cron}")
    @Transactional
    public void checkAndExpireStudentAccounts() {
        if (!cleanupEnabled) {
            log.info("Student account cleanup is disabled.");
            return;
        }

        log.info("Starting student account expiry check...");

        int currentYear = Year.now().getValue();
        List<User> expiredStudents = userRepository.findExpiredStudentAccounts(currentYear);

        if (expiredStudents.isEmpty()) {
            log.info("No expired student accounts found.");
            return;
        }

        log.info("Found {} expired student accounts", expiredStudents.size());

        for (User student : expiredStudents) {
            log.info("Expiring student account: {} (Graduation Year: {})", student.getEmail(), student.getGradYear());

            student.setStatus(UserStatus.EXPIRED);
            student.setAccountNotExpired(false);
            student.setAccountNotLocked(true);
            userRepository.save(student);

            //TODO: send notification to student about account expiry which will ne handled by notification service via kafka
        }

        log.info("Student account expiry check completed. {} accounts expired", expiredStudents.size());
    }

    /**
     * Runs weekly to send warnings to students nearing account expiry after graduation
     */

    @Scheduled(cron = "0 0 9 * * MON") //Every Monday at 9:00 AM
    @Transactional
    public void sendExpiryWarnings() {
        if (!cleanupEnabled) {
            return;
        }

        log.info("checking for student accounts nearing graduation...");

        int currentYear = Year.now().getValue();
        int warningYear = currentYear + 1; //students graduating next year

        List<User> studentsNearingExpiry = userRepository.findStudentsNearingExpiry(warningYear);

        if (studentsNearingExpiry.isEmpty()) {
            log.info("No student accounts nearing graduation found.");
            return;
        }

        log.info("Found {} student accounts nearing graduation", studentsNearingExpiry.size());

        for (User student : studentsNearingExpiry) {
            log.info("Sending expiry warning to student: {} (Graduation Year: {})", student.getEmail(), student.getGradYear());

            student.setExpiryWarningEmailSentAt(LocalDateTime.now());
            userRepository.save(student);

            //TODO: send warning notification via kafka to student about upcoming account expiry by notification service
        }

        log.info("Expiry warning process completed. {} warnings sent", studentsNearingExpiry.size());
    }

    /**
     * Optionally delete expired accounts after a grace period
     * Runs monthly on the 1st at 3 AM
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    @Transactional
    public void deleteExpiredAccounts() {
        if (!cleanupEnabled) {
            return;
        }

        log.info("Starting deletion of long-expired student accounts...");

        // Delete accounts expired for more than 6 months
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        List<User> longExpiredStudents = userRepository.findByRoleAndStatus(UserRole.STUDENT, UserStatus.EXPIRED)
                .stream()
                .filter(user -> user.getUpdatedAt().isBefore(sixMonthsAgo))
                .toList();

        if (longExpiredStudents.isEmpty()) {
            log.info("No long-expired student accounts to delete.");
            return;
        }

        log.info("Found and deleting {} long-expired student accounts", longExpiredStudents.size());

        for (User student : longExpiredStudents) {
            log.info("Deleting expired student account: {} (Expired since: {})", student.getEmail(), student.getUpdatedAt());
            userRepository.delete(student);
        }

        log.info("Deletion of long-expired student accounts completed. {} accounts deleted", longExpiredStudents.size());
    }
}
