package om.example.om_pay.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import om.example.om_pay.entity.enums.StatutTransaction;
import om.example.om_pay.entity.enums.TypeTransaction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;

    @Column(nullable = false)
    private Double montant;

    private Double frais = 0.0;

    @Column(nullable = false)
    private Double montantTotal;

    @ManyToOne
    @JoinColumn(name = "compte_expediteur_id")
    private Compte compteExpediteur;

    @ManyToOne
    @JoinColumn(name = "compte_destinataire_id")
    private Compte compteDestinataire;

    @ManyToOne
    @JoinColumn(name = "marchand_id")
    private Marchand marchand;

    @ManyToOne
    @JoinColumn(name = "distributeur_id")
    private Utilisateur distributeur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statut;

    private String description;
    private String messageErreur;

    @Column(nullable = false)
    private LocalDateTime dateTransaction = LocalDateTime.now();

    private LocalDateTime dateTraitement;





}
