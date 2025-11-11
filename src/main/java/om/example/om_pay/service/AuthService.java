package om.example.om_pay.service;

import om.example.om_pay.dto.request.ChangePasswordRequest;
import om.example.om_pay.dto.request.LoginRequest;
import om.example.om_pay.dto.request.RegisterRequest;
import om.example.om_pay.dto.response.AuthResponse;

public interface AuthService {


    AuthResponse register(RegisterRequest request);

    /**
     * Étape 1 du login : vérification des credentials et envoi d'OTP
     */
    void initiateLogin(LoginRequest request);

    /**
     * Étape 2 du login : vérification de l'OTP et génération du token
     */
    AuthResponse completeLoginWithOtp(String telephone, String otpCode);

    void changePassword(ChangePasswordRequest request);

    void logout(String token);

    AuthResponse refreshToken(String refreshToken);

    boolean telephoneExists(String telephone);

    boolean emailExists(String email);
}
