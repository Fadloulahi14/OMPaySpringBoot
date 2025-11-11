package om.example.om_pay.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import om.example.om_pay.dto.mapper.UtilisateurMapper;
import om.example.om_pay.dto.request.ChangePasswordRequest;
import om.example.om_pay.dto.request.LoginRequest;
import om.example.om_pay.dto.request.RegisterRequest;
import om.example.om_pay.dto.response.AuthResponse;
import om.example.om_pay.exception.BadRequestException;
import om.example.om_pay.exception.UnauthorizedException;
import om.example.om_pay.service.AuthService;
import om.example.om_pay.service.OtpService;
import om.example.om_pay.entity.Compte;
import om.example.om_pay.entity.Utilisateur;
import om.example.om_pay.entity.enums.Statut;
import om.example.om_pay.entity.enums.TypeCompte;
import om.example.om_pay.repository.CompteRepository;
import om.example.om_pay.repository.UtilisateurRepository;
import om.example.om_pay.security.JwtTokenProvider;

/**
 * Implémentation du service d'authentification
 */
@Service
public class AuthServiceImpl implements AuthService {


    private final UtilisateurRepository utilisateurRepository;


    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    public AuthServiceImpl(UtilisateurRepository utilisateurRepository){
        this.utilisateurRepository=utilisateurRepository;
    }
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Vérifier si le téléphone existe déjà
        if (utilisateurRepository.existsByTelephone(request.getTelephone())) {
            throw new BadRequestException("Ce numéro de téléphone est déjà utilisé");
        }

        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Cet email est déjà utilisé");
        }

        // Créer l'utilisateur avec MapStruct
        Utilisateur utilisateur = utilisateurMapper.toEntity(request);
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setCodePin(passwordEncoder.encode(request.getCodePin()));
        utilisateur.setRole(request.getRole());
        utilisateur.setStatut(Statut.ACTIF);
        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setPlafondQuotidien(1000000.0); // 1 000 000 FCFA par défaut
        utilisateur.setTotalTransfertJour(0.0);

        // Sauvegarder l'utilisateur
        utilisateur = utilisateurRepository.save(utilisateur);

        // Créer automatiquement un compte principal
        Compte compte = new Compte();
        compte.setNumeroCompte(genererNumeroCompte());
        compte.setSolde(0.0);
        compte.setTypeCompte(TypeCompte.PRINCIPAL);
        compte.setStatut(Statut.ACTIF);
        compte.setDateCreation(LocalDateTime.now());
        compte.setUtilisateur(utilisateur);
        
        compteRepository.save(compte);

        // Générer le token JWT
        String token = tokenProvider.generateToken(utilisateur.getTelephone());

        // Utiliser MapStruct pour créer la réponse
        AuthResponse authResponse = utilisateurMapper.toAuthResponse(utilisateur);
        authResponse.setToken(token);

        return authResponse;
    }

    @Override
    public void initiateLogin(LoginRequest request) {
        // Rechercher l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(request.getTelephone())
                .orElseThrow(() -> new UnauthorizedException("Téléphone ou mot de passe incorrect"));

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new UnauthorizedException("Téléphone ou mot de passe incorrect");
        }

        // Vérifier le statut du compte
        if (utilisateur.getStatut() == Statut.BLOQUE) {
            throw new UnauthorizedException("Votre compte est bloqué. Contactez le support.");
        }

        // Convertir le numéro de téléphone au format international pour Twilio
        String phoneNumber = convertToInternationalFormat(request.getTelephone());

        // Envoyer l'OTP
        otpService.sendOtp(phoneNumber);
    }

    @Override
    public AuthResponse completeLoginWithOtp(String telephone, String otpCode) {
        // Convertir le numéro au format international pour la vérification OTP
        String phoneNumber = convertToInternationalFormat(telephone);
        boolean isValidOtp = otpService.verifyOtp(phoneNumber, otpCode);

        if (!isValidOtp) {
            throw new UnauthorizedException("Code OTP invalide ou expiré");
        }

        // Rechercher l'utilisateur avec le numéro original (sans conversion)
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone)
                .orElseThrow(() -> new UnauthorizedException("Utilisateur non trouvé"));

        // Générer le token JWT
        String token = tokenProvider.generateToken(utilisateur.getTelephone());

        // Utiliser MapStruct pour créer la réponse
        AuthResponse authResponse = utilisateurMapper.toAuthResponse(utilisateur);
        authResponse.setToken(token);

        return authResponse;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        // Rechercher l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(request.getTelephone())
                .orElseThrow(() -> new BadRequestException("Utilisateur non trouvé"));

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(request.getAncienMotDePasse(), utilisateur.getMotDePasse())) {
            throw new UnauthorizedException("Ancien mot de passe incorrect");
        }

        // Changer le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void logout(String token) {
        
    }

    @Override
    public AuthResponse refreshToken(String oldToken) {
        // Valider le token
        if (!tokenProvider.validateToken(oldToken)) {
            throw new UnauthorizedException("Token invalide");
        }

        // Extraire le téléphone et générer un nouveau token
        String telephone = tokenProvider.getPhoneFromToken(oldToken);
        String newToken = tokenProvider.generateToken(telephone);

        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone)
                .orElseThrow(() -> new UnauthorizedException("Utilisateur non trouvé"));

        // Utiliser MapStruct pour créer la réponse
        AuthResponse authResponse = utilisateurMapper.toAuthResponse(utilisateur);
        authResponse.setToken(newToken);

        return authResponse;
    }

    @Override
    public boolean telephoneExists(String telephone) {
        return utilisateurRepository.existsByTelephone(telephone);
    }

    @Override
    public boolean emailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

   
    private String genererNumeroCompte() {
        String numeroCompte;
        do {
            long numero = (long) (Math.random() * 10_000_000_000L);
            numeroCompte = String.format("OM%010d", numero);
        } while (compteRepository.existsByNumeroCompte(numeroCompte));

        return numeroCompte;
    }

    /**
     * Convertit un numéro de téléphone sénégalais au format international
     * Exemple: "778012731" -> "+221778012731"
     */
    private String convertToInternationalFormat(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être null ou vide");
        }

        // Supprimer tous les espaces et caractères non numériques
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Vérifier si c'est déjà au format international
        if (cleanedNumber.startsWith("221")) {
            return "+" + cleanedNumber;
        }

        // Pour les numéros sénégalais (9 chiffres), ajouter l'indicatif
        if (cleanedNumber.length() == 9 && (cleanedNumber.startsWith("77") || cleanedNumber.startsWith("78") ||
            cleanedNumber.startsWith("76") || cleanedNumber.startsWith("70") || cleanedNumber.startsWith("75"))) {
            return "+221" + cleanedNumber;
        }

        // Si le numéro a déjà l'indicatif sans le +
        if (cleanedNumber.length() == 12 && cleanedNumber.startsWith("221")) {
            return "+" + cleanedNumber;
        }

        throw new IllegalArgumentException("Format de numéro de téléphone invalide: " + phoneNumber);
    }
}
