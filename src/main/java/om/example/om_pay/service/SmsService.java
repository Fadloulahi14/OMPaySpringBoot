package om.example.om_pay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import com.twilio.exception.ApiException;
// import com.twilio.rest.api.v2010.account.Message;
// import com.twilio.type.PhoneNumber;

/**
 * Service responsable de l'envoi des SMS via Twilio
 * Séparé du service OTP pour respecter le principe de responsabilité unique
 */
@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    /**
     * Envoie un SMS avec le code OTP
     */
    public void sendOtpSms(String phoneNumber, String otpCode) {
        try {
            String messageBody = "Votre code de vérification OM Pay est : " + otpCode + ". Valide pendant 5 minutes.";

            // Simulation de l'envoi SMS pour les tests
            logger.info("SMS OTP simulé envoyé au numéro {} - Code: {}", phoneNumber, otpCode);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du SMS au {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi du code de vérification.", e);
        }
    }

    /**
     * Envoie un SMS générique (pour extensibilité future)
     */
    public void sendSms(String phoneNumber, String message) {
        try {
            // Simulation de l'envoi SMS pour les tests
            logger.info("SMS simulé envoyé au numéro {} - Message: {}", phoneNumber, message);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du SMS au {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi du message.", e);
        }
    }
}