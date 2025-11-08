package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.ProductHandler;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/branches/{branchId}/products",
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "addProduct",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Agregar producto a sucursal",
                            operationId = "addProduct",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, required = true, description = "ID de la sucursal")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.ProductDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Producto creado exitosamente")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/products/{productId}",
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "deleteProduct",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Eliminar producto",
                            operationId = "deleteProduct",
                            parameters = {
                                    @Parameter(name = "productId", in = ParameterIn.PATH, required = true, description = "ID del producto")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Producto eliminado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/products/stock",
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateProductStock",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Actualizar stock de producto",
                            operationId = "updateProductStock",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.UpdateProductStockRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Stock actualizado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/products/name",
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateProductName",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Actualizar nombre de producto",
                            operationId = "updateProductName",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.UpdateProductNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Nombre actualizado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{franchiseId}/max-stock",
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "getMaxStockByFranchise",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Obtener producto con mayor stock por sucursal de una franquicia",
                            operationId = "getMaxStockByFranchise",
                            parameters = {
                                    @Parameter(name = "franchiseId", in = ParameterIn.PATH, required = true, description = "ID de la franquicia")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de productos con mayor stock por sucursal")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route(POST("/api/branches/{branchId}/products"), handler::addProduct)
                .andRoute(DELETE("/api/products/{productId}"), handler::deleteProduct)
                .andRoute(PUT("/api/products/stock"), handler::updateProductStock)
                .andRoute(PUT("/api/products/name"), handler::updateProductName)
                .andRoute(GET("/api/franchises/{franchiseId}/max-stock"), handler::getMaxStockByFranchise);
    }
}

