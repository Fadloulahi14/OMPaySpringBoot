package om.example.om_pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import om.example.om_pay.dto.request.ChangePasswordRequest;
import om.example.om_pay.dto.request.LoginRequest;
import om.example.om_pay.dto.request.RegisterRequest;
import om.example.om_pay.config.ApiResponse;
import om.example.om_pay.dto.response.AuthResponse;
import om.example.om_pay.dto.response.UtilisateurResponse;
import om.example.om_pay.service.AuthService;
import om.example.om_pay.entity.Utilisateur;
import om.example.om_pay.repository.UtilisateurRepository;
import om.example.om_pay.utils.CookieUtil;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CookieUtil cookieUtil;

 
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        
        AuthResponse authResponse = authService.register(request);
        
        cookieUtil.createJwtCookie(authResponse.getToken(), response);
       
        ApiResponse<AuthResponse> apiResponse = ApiResponse.success(
            "Inscription réussie. Token disponible dans cookie et réponse.",
            authResponse
        );
        
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    /**
     * Étape 1 : Initiation de la connexion (vérification credentials + envoi OTP)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> initiateLogin(
            @Valid @RequestBody LoginRequest request) {

        authService.initiateLogin(request);

        ApiResponse<String> apiResponse = ApiResponse.success(
            "Code OTP envoyé à votre numéro de téléphone. Veuillez le saisir pour finaliser la connexion.",
            null
        );

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Étape 2 : Finalisation de la connexion avec OTP
     * POST /api/auth/verify-login
     * Token stocké dans cookie HTTP-only ET retourné dans la réponse
     */
    @PostMapping("/verify-login")
    public ResponseEntity<ApiResponse<AuthResponse>> completeLoginWithOtp(
            @Valid @RequestBody VerifyLoginRequest request,
            HttpServletResponse response) {

        AuthResponse authResponse = authService.completeLoginWithOtp(request.getTelephone(), request.getOtpCode());

        cookieUtil.createJwtCookie(authResponse.getToken(), response);

        ApiResponse<AuthResponse> apiResponse = ApiResponse.success(
            "Connexion réussie. Token disponible dans cookie et réponse.",
            authResponse
        );

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * DTO pour la vérification du login avec OTP
     */
    public static class VerifyLoginRequest {
        @NotBlank(message = "Le numéro de téléphone est obligatoire")
        private String telephone;

        @NotBlank(message = "Le code OTP est obligatoire")
        @Pattern(regexp = "^[0-9]{6}$", message = "Le code OTP doit contenir exactement 6 chiffres")
        private String otpCode;

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getOtpCode() {
            return otpCode;
        }

        public void setOtpCode(String otpCode) {
            this.otpCode = otpCode;
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        
        ApiResponse<String> response = ApiResponse.success(
            "Mot de passe changé avec succès",
            null
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtenir les informations de l'utilisateur connecté
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String telephone = authentication.getName();
        
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        UtilisateurResponse userResponse = new UtilisateurResponse();
        userResponse.setId(utilisateur.getId());
        userResponse.setNom(utilisateur.getNom());
        userResponse.setPrenom(utilisateur.getPrenom());
        userResponse.setTelephone(utilisateur.getTelephone());
        userResponse.setEmail(utilisateur.getEmail());
        userResponse.setRole(utilisateur.getRole());
        userResponse.setStatut(utilisateur.getStatut());
        userResponse.setDateCreation(utilisateur.getDateCreation());
        
        ApiResponse<UtilisateurResponse> response = ApiResponse.success(userResponse);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Rafraîchir le token JWT
     * POST /api/auth/refresh-token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String token) {
        // Extraire le token du header "Bearer <token>"
        String jwt = token.substring(7);
        
        AuthResponse authResponse = authService.refreshToken(jwt);
        
        ApiResponse<AuthResponse> response = ApiResponse.success(
            "Token rafraîchi avec succès",
            authResponse
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Déconnexion
     * POST /api/auth/logout
     * Supprime le cookie JWT
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        // Supprimer le cookie JWT
        cookieUtil.deleteJwtCookie(response);
        
        ApiResponse<String> apiResponse = ApiResponse.success(
            "Déconnexion réussie. Cookie supprimé.",
            null
        );
        
        return ResponseEntity.ok(apiResponse);
    }
}
