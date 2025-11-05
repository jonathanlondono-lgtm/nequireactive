package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.ProductHandler;
import io.swagger.v3.oas.annotations.Operation;
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
                    path = "/api/v1/products",
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "addProduct",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Agregar producto a sucursal",
                            operationId = "addProduct",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.ProductRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products",
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "deleteProduct",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Eliminar producto",
                            operationId = "deleteProduct",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.DeleteProductRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Producto eliminado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products/stock",
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateProductStock",
                    operation = @Operation(
                            tags = {"Product"},
                            summary = "Actualizar stock de producto",
                            operationId = "updateProductStock",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.request.UpdateStockRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Stock actualizado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products/name",
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
            )
    })
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.nest(path("/api/v1/products"),
                RouterFunctions.route(POST(""), handler::addProduct)
                        .andRoute(DELETE(""), handler::deleteProduct)
                        .andRoute(PUT("/stock"), handler::updateProductStock)
                        .andRoute(PUT("/name"), handler::updateProductName)
        );
    }
}