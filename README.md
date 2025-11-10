 API REST complète pour la gestion des transactions mobiles Orange Money.
    
    ## Fonctionnalités principales
    - 💰 **Transactions** : Dépôts, retraits, transferts, paiements
    - 👥 **Gestion utilisateurs** : Clients, distributeurs, marchands, admin
    - 💳 **Gestion comptes** : Consultation solde, historique, blocage/déblocage
    - 🔐 **Authentification JWT** : Tokens sécurisés avec cookies HTTP-only
    
    ## Authentification
    1. Utilisez l'endpoint `/api/auth/login` pour vous connecter
    2. Copiez le token depuis la réponse (il est aussi dans un cookie HTTP-only)
    3. Cliquez sur "Authorize" et collez le token
    4. Testez les endpoints protégés
    
    ## Données de test (utilisateurs sans code PIN)
    | Rôle | Nom | Téléphone | Mot de passe | Numéro de compte |
    |------|-----|-----------|--------------|------------------|
    | CLIENT | Moussa Diop | 771234567 | Password123! | OM8000380279 |
    | CLIENT | Fatou Sarr | 779876543 | Pass123! | OM2665616523 |
    | CLIENT | Cheikh Fall | 776543210 | Client123! | OM5432147504 |
    | DISTRIBUTEUR | Abdou Ndiaye | 775551234 | Distrib123! | OM4274060223 |
    
