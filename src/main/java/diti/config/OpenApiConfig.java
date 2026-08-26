package diti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Produits")
                        .version("1.0")
                        .description("""
                                API REST de gestion des produits et de leurs types.

                                Toutes les erreurs sont renvoyees au format `ApiError`, qui contient
                                un champ `code` (voir l'enumeration `ErrorCode`) stable et exploitable
                                par le client, independamment du texte du message.
                                """)
                        .contact(new Contact().name("DITI4")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Environnement local")));
    }
}
