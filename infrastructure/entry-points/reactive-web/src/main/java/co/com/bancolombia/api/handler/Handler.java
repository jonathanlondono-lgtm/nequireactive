package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.FranchiseDTO;
import co.com.bancolombia.usecase.franchiseusecase.FranchiseUsecaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUsecaseUseCase useCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(dto -> useCase.createFranchise(dto.getName()))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise));
    }

}
