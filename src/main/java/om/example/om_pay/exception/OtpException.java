package om.example.om_pay.exception;

/**
 * Exception pour les erreurs liées aux OTP
 */
public class OtpException extends RuntimeException {

    public OtpException(String message) {
        super(message);
    }

    public OtpException(String message, Throwable cause) {
        super(message, cause);
    }
}