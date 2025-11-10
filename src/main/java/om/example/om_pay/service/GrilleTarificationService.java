package om.example.om_pay.service;

import java.util.List;

import om.example.om_pay.entity.GrilleTarification;
import om.example.om_pay.entity.enums.TypeTransaction;

public interface GrilleTarificationService {
  
    GrilleTarification creer(GrilleTarification grille);
    
    Double calculerFrais(Double montant, TypeTransaction typeTransaction);

    List<GrilleTarification> getAll();

    List<GrilleTarification> getByTypeTransaction(TypeTransaction typeTransaction);
    
    GrilleTarification update(Long id, GrilleTarification grille);
    
    void delete(Long id);
}
