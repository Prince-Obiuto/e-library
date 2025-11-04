package ng.edu.futo.sict_e_library.user_service.exception;

public class AccountExpiredException extends RuntimeException {
    public AccountExpiredException(String message) {
        super(message);
    }
}
