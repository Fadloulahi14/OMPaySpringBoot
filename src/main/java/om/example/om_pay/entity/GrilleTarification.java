package om.example.om_pay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import om.example.om_pay.entity.enums.Statut;
import om.example.om_pay.entity.enums.TypeTransaction;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grille_tarification")
public class GrilleTarification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;

    @Column(nullable = false)
    private Double montantMin;

    @Column(nullable = false)
    private Double montantMax;

    @Column(nullable = false)
    private Double frais;

    private Boolean pourcentage = false;  // Si true, frais est un %

    @Enumerated(EnumType.STRING)
    private Statut statut;


}
