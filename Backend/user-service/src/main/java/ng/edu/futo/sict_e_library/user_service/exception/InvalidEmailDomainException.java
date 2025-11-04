package ng.edu.futo.sict_e_library.user_service.exception;

public class InvalidEmailDomainException extends RuntimeException {
    public InvalidEmailDomainException(String message) {
        super(message);
    }
}
