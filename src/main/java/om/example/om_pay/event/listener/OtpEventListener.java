package om.example.om_pay.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import om.example.om_pay.event.OtpRequestedEvent;
import om.example.om_pay.event.OtpVerifiedEvent;
import om.example.om_pay.service.SmsService;

/**
 * Listener pour les événements OTP
 * Responsable de l'envoi des SMS en réponse aux événements OTP
 */
@Component
public class OtpEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OtpEventListener.class);

    @Autowired
    private SmsService smsService;

    /**
     * Écoute l'événement de demande d'OTP et envoie le SMS
     */
    @EventListener
    @Async
    public void handleOtpRequested(OtpRequestedEvent event) {
        try {
            logger.info("Traitement de la demande OTP pour le numéro: {}", event.getPhoneNumber());

            // Envoi du SMS avec le code OTP
            smsService.sendOtpSms(event.getPhoneNumber(), event.getOtpCode());

            logger.info("SMS OTP envoyé avec succès pour le numéro: {}", event.getPhoneNumber());

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du SMS OTP pour le numéro {}: {}",
                        event.getPhoneNumber(), e.getMessage());
            // Note: Dans un système réel, on pourrait vouloir déclencher un événement d'échec
            // ou utiliser un système de retry/queue pour les SMS échoués
        }
    }

    /**
     * Écoute l'événement de vérification réussie d'OTP
     * Peut être utilisé pour des actions supplémentaires (logging, notifications, etc.)
     */
    @EventListener
    public void handleOtpVerified(OtpVerifiedEvent event) {
        logger.info("OTP vérifié avec succès pour le numéro: {}", event.getPhoneNumber());

        // Ici, on pourrait ajouter d'autres actions comme :
        // - Envoyer une notification de connexion réussie
        // - Mettre à jour des statistiques
        // - Déclencher d'autres événements métier
        // - etc.
    }
}