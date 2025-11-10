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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import om.example.om_pay.entity.enums.Statut;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "marchand")
public class Marchand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomCommercial;

    @Column(unique = true, nullable = false)
    private String numeroMarchand;

    @Column(unique = true, nullable = false)
    private String codeMarchand;

    private String categorie;  
    private String adresse;
    private String email;

    @Enumerated(EnumType.STRING)
    private Statut statut;  // ACTIF, INACTIF

    @Column(nullable = false)
    private Double commission = 0.0;  // % de commission sur chaque paiement

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "marchand")
    private List<Transaction> transactions;




}
