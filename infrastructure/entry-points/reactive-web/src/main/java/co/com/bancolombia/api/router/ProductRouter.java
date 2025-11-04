package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions
                .nest(path("/api/v1/products"),
                        RouterFunctions.route(POST(""), handler::addProduct)
                                .andRoute(DELETE(""), handler::deleteProduct)
                                .andRoute(PUT("/stock"), handler::updateProductStock)
                );
    }
}
