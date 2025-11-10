package om.example.om_pay.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import om.example.om_pay.entity.enums.Role;
import om.example.om_pay.entity.enums.Statut;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String telephone;

    private String email;

    @Column(length = 255, nullable = false)
    private String motDePasse; 

    @Column(length = 255, nullable = true)
    private String codePin;  // Code PIN optionnel (nullable)

    @Enumerated(EnumType.STRING)
    private Role role;  

    @Enumerated(EnumType.STRING)
    private Statut statut; 

    private Double plafondQuotidien = 500000.0;  // Limite par jour
    private Double totalTransfertJour = 0.0;     // Suivi des transferts du jour
    private LocalDate dernierResetPlafond;       // Pour réinitialiser chaque jour

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Compte> comptes;

    @OneToMany(mappedBy = "distributeur")
    private List<Transaction> operationsDistributeur;


}
