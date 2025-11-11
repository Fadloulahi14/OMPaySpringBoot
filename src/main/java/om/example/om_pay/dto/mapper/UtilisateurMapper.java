package om.example.om_pay.dto.mapper;

import om.example.om_pay.dto.request.RegisterRequest;
import om.example.om_pay.dto.response.AuthResponse;
import om.example.om_pay.dto.response.UtilisateurResponse;
import om.example.om_pay.entity.Utilisateur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {


    Utilisateur toEntity(RegisterRequest request);
    UtilisateurResponse toResponse(Utilisateur utilisateur);
    AuthResponse toAuthResponse(Utilisateur utilisateur);
}