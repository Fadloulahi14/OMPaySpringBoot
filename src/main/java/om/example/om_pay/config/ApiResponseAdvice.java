package om.example.om_pay.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request, ServerHttpResponse response) {

        // Vérifier si c'est une requête vers les endpoints SpringDoc
        String path = request.getURI().getPath();
        if (path != null && (path.startsWith("/v3/api-docs") || path.startsWith("/swagger"))) {
            return body;
        }

        // Si le body est déjà un ApiResponse, le retourner tel quel
        if (body instanceof ApiResponse) {
            return body;
        }

        // Si le body est null, retourner un succès sans data
        if (body == null) {
            return ApiResponse.success("Opération réussie");
        }

        // Pour les autres types, wrapper dans ApiResponse
        return ApiResponse.success("Opération réussie", body);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Exclure les endpoints Swagger/OpenAPI pour éviter les conflits
        String requestPath = returnType.getExecutable().getDeclaringClass().getPackageName();
        if (requestPath != null && (requestPath.contains("springdoc") || requestPath.contains("swagger"))) {
            return false;
        }

        // Vérifier si c'est un endpoint SpringDoc
        if (returnType.getContainingClass() != null) {
            String className = returnType.getContainingClass().getSimpleName();
            if (className.contains("OpenApi") || className.contains("Swagger") ||
                className.contains("ApiResource") || className.contains("WebMvc")) {
                return false;
            }
        }

        // Exclure les requêtes vers /v3/api-docs
        try {
            // Cette approche peut ne pas marcher dans tous les cas, mais essayons
            org.springframework.web.context.request.ServletRequestAttributes attrs =
                (org.springframework.web.context.request.ServletRequestAttributes)
                org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes();
            jakarta.servlet.http.HttpServletRequest request = attrs.getRequest();
            if (request != null && request.getRequestURI().contains("/v3/api-docs")) {
                return false;
            }
        } catch (Exception e) {
            // Ignore l'exception si on ne peut pas accéder à la requête
        }

        // Exclure les endpoints d'authentification qui retournent des String simples
        if (returnType.getContainingClass() != null &&
            returnType.getContainingClass().getSimpleName().equals("AuthController") &&
            returnType.getParameterType().equals(String.class)) {
            return false;
        }

        return !returnType.getParameterType().equals(ApiResponse.class);
    }

}