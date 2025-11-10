package om.example.om_pay.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import om.example.om_pay.entity.enums.Statut;
import om.example.om_pay.entity.enums.TypeCompte;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "compte")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCompte;  

    @Column(nullable = false)
    private Double solde = 0.0;

    private String devise = "XOF";  

    @Enumerated(EnumType.STRING)
    private TypeCompte typeCompte; 

    @Enumerated(EnumType.STRING)
    private Statut statut; 

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "compteExpediteur")
    private List<Transaction> transactionsEnvoyees;

    @OneToMany(mappedBy = "compteDestinataire")
    private List<Transaction> transactionsRecues;




}
