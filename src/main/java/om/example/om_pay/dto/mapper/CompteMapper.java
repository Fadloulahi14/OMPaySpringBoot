package om.example.om_pay.dto.mapper;

import om.example.om_pay.dto.response.CompteResponse;
import om.example.om_pay.entity.Compte;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompteMapper {

   
    CompteResponse toResponse(Compte compte);
}