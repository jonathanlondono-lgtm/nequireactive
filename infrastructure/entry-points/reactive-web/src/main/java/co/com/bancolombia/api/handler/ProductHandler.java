package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.DeleteProductRequest;
import co.com.bancolombia.api.dto.request.ProductRequest;
import co.com.bancolombia.api.dto.request.UpdateStockRequest;
import co.com.bancolombia.api.dto.response.UpdateStockResponse;
import co.com.bancolombia.usecase.franchiseusecase.ProductUseCase;
import jakarta.validation.Validator;
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
    private final Validator validator;

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        return ServerResponse.badRequest().bodyValue(violations);
                    }

                    return productUseCase.execute(dto.getBranchId(), dto.getName(), dto.getStock())
                            .then(ServerResponse.status(HttpStatus.CREATED).build());


                });
    }
    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return request.bodyToMono(DeleteProductRequest.class)
                .flatMap(dto ->
                        productUseCase.execute(dto.getBranchId(), dto.getProductId())
                                .then(ServerResponse.noContent().build())
                );
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(dto ->
                        productUseCase.execute(dto.getProductId(), dto.getNewStock())
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
}