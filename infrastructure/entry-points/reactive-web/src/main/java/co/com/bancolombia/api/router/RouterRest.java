package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.Handler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchise",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "createFranchise",
                    operation = @Operation(
                            tags = {"Franchise"},
                            summary = "Crear franquicia",
                            operationId = "createFranchise",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = co.com.bancolombia.api.dto.request.FranchiseDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franquicia creada exitosamente"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchise/max-stock",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "getMaxStockByFranchise",
                    operation = @Operation(
                            tags = {"Franchise"},
                            summary = "Obtener productos con mayor stock por franquicia",
                            operationId = "getMaxStockByFranchise",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.MaxStockByFranchiseRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de productos con mayor stock")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchise/name",
                    method = RequestMethod.PUT,
                    beanClass = Handler.class,
                    beanMethod = "updateFranchiseName",
                    operation = @Operation(
                            tags = {"Franchise"},
                            summary = "Actualizar nombre de franquicia",
                            operationId = "updateFranchiseName",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.UpdateFranchiseNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/franchise"), handler::createFranchise)
                .andRoute(POST("/api/franchise/max-stock"), handler::getMaxStockByFranchise)
                .andRoute(PUT("/api/franchise/name"), handler::updateFranchiseName);
    }
}