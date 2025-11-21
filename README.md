jdbc:postgresql://ballast.proxy.rlwy.net:44054/railway
postgres

qaPPTWkqUEngIkSozVbfwWvgqNMrxWou

spring.datasource.url=
spring.datasource.username=postgres
spring.datasource.password=qaPPTWkqUEngIkSozVbfwWvgqNMrxWou


DATABASE_URL=jdbc:postgresql://ballast.proxy.rlwy.net:44054/railway
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=qaPPTWkqUEngIkSozVbfwWvgqNMrxWou

TWILIO_ACCOUNT_SID=AC7172efe088c69ff0df4cf40ad2544447
TWILIO_AUTH_TOKEN=c52a0576f93789746e13392606ea1775
TWILIO_PHONE_NUMBER=+12052891936
SPRING_PROFILE = prod


spring.datasource.url=jdbc:postgresql://ballast.proxy.rlwy.net:44054/railway
spring.datasource.username=postgres
spring.datasource.password=qaPPTWkqUEngIkSozVbfwWvgqNMrx


spring.application.name=om_pay
server.port=${PORT:8083}
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=${JPA_SHOW_SQL:true}
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
spring.jpa.open-in-view=false
jwt.secret=${JWT_SECRET:OmPaySecretKey2025VerySecureAndLongKeyForProductionUseOnlyWithExtraCharactersToMeet512BitsRequirement}
jwt.expiration=${JWT_EXPIRATION:86400000}
spring.messages.basename=messages/error-messages
spring.messages.encoding=UTF-8
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss
spring.jackson.time-zone=Africa/Dakar
spring.jackson.default-property-inclusion=non_null
logging.level.root=INFO
logging.level.om.example.om_pay=DEBUG
logging.level.org.springframework.security=DEBUG
logging.file.name=logs/om_pay.log
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=30
spring.profiles.active=${SPRING_PROFILE:dev}
# ==============================================
# SPRINGDOC OPENAPI CONFIGURATION
# ==============================================

# Activer la génération automatique des API docs
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs

# Configuration Swagger UI
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html

# URL automatique vers vos API docs (pas besoin de saisir manuellement)
springdoc.swagger-ui.url=/v3/api-docs

# Configuration de l'interface
springdoc.swagger-ui.operationsSorter=alpha
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.displayRequestDuration=true
springdoc.swagger-ui.defaultModelsExpandDepth=1
springdoc.swagger-ui.defaultModelExpandDepth=3
springdoc.swagger-ui.tryItOutEnabled=true

# Désactiver les URLs par défaut pour éviter le chargement de Petstore
springdoc.swagger-ui.disable-swagger-default-url=true
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
twilio.account.sid=${TWILIO_ACCOUNT_SID:AC7172efe088c69ff0df4cf40ad2544447}
twilio.auth.token=${TWILIO_AUTH_TOKEN:c52a0576f93789746e13392606ea1775}
twilio.phone.number=${TWILIO_PHONE_NUMBER:+12052891936}