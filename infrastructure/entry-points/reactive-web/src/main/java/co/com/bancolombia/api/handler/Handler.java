package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.FranchiseDTO;
import co.com.bancolombia.api.dto.request.MaxStockByFranchiseRequest;
import co.com.bancolombia.api.dto.response.MaxStockByBranchResponse;
import co.com.bancolombia.usecase.franchiseusecase.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase useCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(dto -> useCase.createFranchise(dto.getName()))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
    }

    public Mono<ServerResponse> getMaxStockByFranchise(ServerRequest request) {
        return request.bodyToMono(MaxStockByFranchiseRequest.class)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(useCase.getProductsWithHighestStockByFranchise(dto.getFranchiseId()), MaxStockByBranchResponse.class)
                );
    }

}
