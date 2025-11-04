package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/franchise"), handler::createFranchise)
                .andRoute(POST("/api/franchise/max-stock"), handler::getMaxStockByFranchise)
                .andRoute(PUT("/api/franchise/name"), handler::updateFranchiseName);


    }
}
