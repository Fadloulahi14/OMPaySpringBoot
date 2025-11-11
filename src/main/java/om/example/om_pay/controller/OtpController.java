package om.example.om_pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import om.example.om_pay.config.ApiResponse;
import om.example.om_pay.exception.OtpException;
import om.example.om_pay.service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private MessageSource messageSource;

    /**
     * DTO pour la requête d'envoi d'OTP
     */
    public static class SendOtpRequest {
        @NotBlank(message = "Le numéro de téléphone est obligatoire")
        @Pattern(regexp = "^\\+221[0-9]{9}$", message = "Le numéro doit être au format +221XXXXXXXXX")
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * DTO pour la requête de vérification d'OTP
     */
    public static class VerifyOtpRequest {
        @NotBlank(message = "Le numéro de téléphone est obligatoire")
        @Pattern(regexp = "^\\+221[0-9]{9}$", message = "Le numéro doit être au format +221XXXXXXXXX")
        private String phoneNumber;

        @NotBlank(message = "Le code OTP est obligatoire")
        @Pattern(regexp = "^[0-9]{6}$", message = "Le code OTP doit contenir exactement 6 chiffres")
        private String code;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * Envoi d'un code OTP par SMS
     * POST /api/otp/send
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        try {
            otpService.sendOtp(request.getPhoneNumber());

            String successMessage = messageSource.getMessage(
                "otp.send.success",
                null,
                "Code OTP envoyé avec succès",
                LocaleContextHolder.getLocale()
            );

            ApiResponse<String> response = ApiResponse.success(successMessage, null);
            return ResponseEntity.ok(response);

        } catch (OtpException e) {
            String errorMessage = messageSource.getMessage(
                "otp.send.error",
                null,
                "Erreur lors de l'envoi du code OTP",
                LocaleContextHolder.getLocale()
            );

            ApiResponse<String> response = ApiResponse.error(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage(
                "otp.send.error",
                null,
                "Erreur inattendue lors de l'envoi du code OTP",
                LocaleContextHolder.getLocale()
            );

            ApiResponse<String> response = ApiResponse.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Vérification du code OTP
     * POST /api/otp/verify
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getCode());

            if (isValid) {
                String successMessage = messageSource.getMessage(
                    "otp.verify.success",
                    null,
                    "Code OTP vérifié avec succès",
                    LocaleContextHolder.getLocale()
                );

                ApiResponse<String> response = ApiResponse.success(successMessage, null);
                return ResponseEntity.ok(response);
            } else {
                String errorMessage = messageSource.getMessage(
                    "otp.verify.invalid",
                    null,
                    "Code OTP invalide ou expiré",
                    LocaleContextHolder.getLocale()
                );

                ApiResponse<String> response = ApiResponse.error(errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage(
                "otp.verify.error",
                null,
                "Erreur lors de la vérification du code OTP",
                LocaleContextHolder.getLocale()
            );

            ApiResponse<String> response = ApiResponse.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}