package om.example.om_pay.dto.mapper;

import om.example.om_pay.dto.response.TransactionResponse;
import om.example.om_pay.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "compteExpediteur", source = "compteExpediteur.numeroCompte")
    @Mapping(target = "compteDestinataire", source = "compteDestinataire.numeroCompte")
    @Mapping(target = "telephoneDistributeur", source = "distributeur.telephone")
    @Mapping(target = "nomMarchand", source = "marchand.nomCommercial")
    TransactionResponse toResponse(Transaction transaction);
}