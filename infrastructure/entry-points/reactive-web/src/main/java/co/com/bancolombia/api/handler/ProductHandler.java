package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.ProductDTO;
import co.com.bancolombia.api.dto.request.UpdateProductNameRequest;
import co.com.bancolombia.api.dto.request.UpdateProductStockRequest;
import co.com.bancolombia.api.dto.response.ProductResponse;
import co.com.bancolombia.api.dto.response.ProductWithBranchResponse;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final RequestValidator validator;

    /**
     * Agregar un producto a una sucursal específica
     */
    public Mono<ServerResponse> addProduct(ServerRequest request) {
        UUID branchId = UUID.fromString(request.pathVariable("branchId"));
        return request.bodyToMono(ProductDTO.class)
                .flatMap(validator::validate)
                .map(dto -> Product.builder()
                        .name(dto.getName())
                        .stock(dto.getStock())
                        .branchId(branchId)
                        .build())
                .flatMap(productUseCase::addProductToBranch)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getStock(),
                        product.getBranchId()
                ))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    /**
     * Eliminar un producto de una sucursal
     */
    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        UUID productId = UUID.fromString(request.pathVariable("productId"));
        return productUseCase.deleteProduct(productId)
                .then(ServerResponse.noContent().build());
    }

    /**
     * Modificar el stock de un producto
     */
    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        return request.bodyToMono(UpdateProductStockRequest.class)
                .flatMap(validator::validate)
                .map(dto -> Product.builder()
                        .id(dto.getProductId())
                        .stock(dto.getNewStock())
                        .build())
                .flatMap(productUseCase::updateProductStock)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getStock(),
                        product.getBranchId()
                ))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    /**
     * Actualizar el nombre de un producto (Plus)
     */
    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        return request.bodyToMono(UpdateProductNameRequest.class)
                .flatMap(validator::validate)
                .map(dto -> Product.builder()
                        .id(dto.getProductId())
                        .name(dto.getNewName())
                        .build())
                .flatMap(productUseCase::updateProductName)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getStock(),
                        product.getBranchId()
                ))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    /**
     * Obtener el producto con mayor stock por cada sucursal de una franquicia
     */
    public Mono<ServerResponse> getMaxStockByFranchise(ServerRequest request) {
        UUID franchiseId = UUID.fromString(request.pathVariable("franchiseId"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productUseCase.getProductsWithHighestStockByFranchise(franchiseId)
                        .map(product -> new ProductWithBranchResponse(
                                product.getBranchId(),
                                product.getId(),
                                product.getName(),
                                product.getStock()
                        )), ProductWithBranchResponse.class);
    }
}

