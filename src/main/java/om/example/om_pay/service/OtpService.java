package om.example.om_pay.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import om.example.om_pay.entity.Otp;
import om.example.om_pay.exception.OtpException;
import om.example.om_pay.repository.OtpRepository;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private OtpRepository otpRepository;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;

    /**
     * Génère un code OTP à 6 chiffres aléatoires
     */
    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Envoie un code OTP par SMS via Twilio
     */
    @Transactional
    public void sendOtp(String phone) {
        try {
            // Nettoyer les anciens OTP expirés
            otpRepository.deleteExpiredOtps(LocalDateTime.now());

            // Générer un nouveau code OTP
            String code = generateOtp();

            // Créer l'entité OTP
            Otp otp = new Otp();
            otp.setPhoneNumber(phone);
            otp.setCode(code);
            otp.setExpirationDateTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));

            // Sauvegarder en base
            otpRepository.save(otp);

            // Envoyer le SMS via Twilio
            Message message = Message.creator(
                new PhoneNumber(phone),
                new PhoneNumber(twilioPhoneNumber),
                "Votre code de vérification OM Pay est : " + code + ". Valide pendant 5 minutes."
            ).create();

            logger.info("OTP envoyé avec succès au numéro {} - SID: {}", phone, message.getSid());

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'OTP au {}: {}", phone, e.getMessage());
            throw new OtpException("Erreur lors de l'envoi du code de vérification.", e);
        }
    }

    /**
     * Vérifie si le code OTP est valide et non expiré
     */
    @Transactional
    public boolean verifyOtp(String phone, String code) {
        try {
            LocalDateTime now = LocalDateTime.now();

            // Chercher l'OTP valide pour ce numéro
            var otpOptional = otpRepository.findByPhoneNumberAndCode(phone, code);

            if (otpOptional.isEmpty()) {
                logger.warn("Tentative de vérification OTP échouée - code invalide pour le numéro {}", phone);
                return false;
            }

            Otp otp = otpOptional.get();

            // Vérifier si l'OTP n'est pas expiré
            if (otp.getExpirationDateTime().isBefore(now)) {
                logger.warn("Tentative de vérification OTP échouée - code expiré pour le numéro {}", phone);
                // Supprimer l'OTP expiré
                otpRepository.delete(otp);
                return false;
            }

            // Supprimer l'OTP après utilisation réussie
            otpRepository.delete(otp);

            logger.info("OTP vérifié avec succès pour le numéro {}", phone);
            return true;

        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'OTP pour {}: {}", phone, e.getMessage());
            return false;
        }
    }
}