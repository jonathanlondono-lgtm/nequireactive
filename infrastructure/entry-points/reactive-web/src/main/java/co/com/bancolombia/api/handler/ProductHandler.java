package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.ProductDTO;
import co.com.bancolombia.api.dto.request.UpdateProductNameRequest;
import co.com.bancolombia.api.dto.request.UpdateProductStockRequest;
import co.com.bancolombia.api.dto.response.ProductWithBranchResponse;
import co.com.bancolombia.api.mapper.ProductApiMapper;
import co.com.bancolombia.api.validator.RequestValidator;
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


    public Mono<ServerResponse> addProduct(ServerRequest request) {
        UUID branchId = UUID.fromString(request.pathVariable("branchId"));
        return request.bodyToMono(ProductDTO.class)
                .flatMap(validator::validate)
                .map(dto -> ProductApiMapper.toDomain(dto, branchId))
                .flatMap(productUseCase::addProductToBranch)
                .map(ProductApiMapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }


    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        UUID productId = UUID.fromString(request.pathVariable("productId"));
        return productUseCase.deleteProduct(productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        return request.bodyToMono(UpdateProductStockRequest.class)
                .flatMap(validator::validate)
                .map(ProductApiMapper::toDomainForStockUpdate)
                .flatMap(productUseCase::updateProductStock)
                .map(ProductApiMapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }


    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        return request.bodyToMono(UpdateProductNameRequest.class)
                .flatMap(validator::validate)
                .map(ProductApiMapper::toDomainForNameUpdate)
                .flatMap(productUseCase::updateProductName)
                .map(ProductApiMapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }


    public Mono<ServerResponse> getMaxStockByFranchise(ServerRequest request) {
        UUID franchiseId = UUID.fromString(request.pathVariable("franchiseId"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productUseCase.getProductsWithHighestStockByFranchise(franchiseId)
                        .map(ProductApiMapper::toProductWithBranchResponse),
                        ProductWithBranchResponse.class);
    }
}

