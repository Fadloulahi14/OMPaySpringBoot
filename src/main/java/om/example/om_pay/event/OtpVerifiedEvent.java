package om.example.om_pay.event;

import org.springframework.context.ApplicationEvent;

/**
 * Événement déclenché lorsqu'un OTP est vérifié avec succès
 */
public class OtpVerifiedEvent extends ApplicationEvent {

    private final String phoneNumber;
    private final String otpCode;

    public OtpVerifiedEvent(Object source, String phoneNumber, String otpCode) {
        super(source);
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOtpCode() {
        return otpCode;
    }
}