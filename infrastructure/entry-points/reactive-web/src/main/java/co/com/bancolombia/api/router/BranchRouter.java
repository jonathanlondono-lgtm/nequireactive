package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

@Configuration
public class BranchRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchises/{id}/branches",
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "addBranch",
                    operation = @Operation(
                            tags = {"Branch"},
                            summary = "Agregar sucursal a franquicia",
                            operationId = "addBranch",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "ID de la franquicia")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.BranchDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Sucursal agregada exitosamente")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/branch/name",
                    method = RequestMethod.PUT,
                    beanClass = BranchHandler.class,
                    beanMethod = "updateBranchName",
                    operation = @Operation(
                            tags = {"Branch"},
                            summary = "Actualizar nombre de sucursal",
                            operationId = "updateBranchName",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.UpdateBranchNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route(POST("/api/franchises/{id}/branches"), handler::addBranch)
                .andRoute(PUT("/api/branch/name"), handler::updateBranchName);
    }
}