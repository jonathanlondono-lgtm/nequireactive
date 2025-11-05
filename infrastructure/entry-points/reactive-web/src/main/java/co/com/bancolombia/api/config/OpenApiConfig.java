package co.com.bancolombia.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Franchise Management API")
                        .version("1.0.0")
                        .description("API para gestión de franquicias, sucursales y productos"))
                .tags(List.of(
                        new Tag().name("Franchise").description("Operaciones de franquicias"),
                        new Tag().name("Branch").description("Operaciones de sucursales"),
                        new Tag().name("Product").description("Operaciones de productos")
                ));
    }
}