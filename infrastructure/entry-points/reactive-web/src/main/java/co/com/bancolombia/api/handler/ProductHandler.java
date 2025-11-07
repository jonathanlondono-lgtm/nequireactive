package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.DeleteProductRequest;
import co.com.bancolombia.api.dto.request.ProductRequest;
import co.com.bancolombia.api.dto.request.UpdateProductNameRequest;
import co.com.bancolombia.api.dto.request.UpdateStockRequest;
import co.com.bancolombia.api.dto.response.CreateProductResponse;
import co.com.bancolombia.api.dto.response.UpdateProductNameResponse;
import co.com.bancolombia.api.dto.response.UpdateStockResponse;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.usecase.franchiseusecase.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final RequestValidator validator;

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(validator::validate)
                .flatMap(dto -> productUseCase.execute(dto.getBranchId(), dto.getName(), dto.getStock())
                        .map(product -> new CreateProductResponse(
                                product.getId(),
                                product.getName(),
                                product.getStock(),
                                dto.getBranchId()
                        ))
                        .flatMap(response ->
                                ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)
                        )
                );
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return request.bodyToMono(DeleteProductRequest.class)
                .flatMap(validator::validate)
                .flatMap(dto -> productUseCase.execute(dto.getBranchId(), dto.getProductId())
                        .then(ServerResponse.noContent().build())
                );
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(validator::validate)
                .flatMap(dto -> productUseCase.execute(dto.getProductId(), dto.getNewStock())
                        .map(product -> new UpdateStockResponse(
                                product.getId(),
                                product.getName(),
                                product.getStock()
                        ))
                        .flatMap(response ->
                                ServerResponse.status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)
                        )
                );
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        return request.bodyToMono(UpdateProductNameRequest.class)
                .flatMap(validator::validate)
                .flatMap(dto -> productUseCase.updateProductName(dto.getProductId(), dto.getNewName())
                        .map(product -> new UpdateProductNameResponse(
                                product.getId(),
                                product.getName()
                        ))
                        .flatMap(response ->
                                ServerResponse.status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)
                        )
                );
    }
}
