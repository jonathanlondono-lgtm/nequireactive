package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.FranchiseDTO;
import co.com.bancolombia.api.dto.request.UpdateFranchiseNameRequest;
import co.com.bancolombia.api.mapper.FranchiseApiMapper;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.usecase.franchiseusecase.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final FranchiseUseCase useCase;
    private final RequestValidator validator;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(validator::validate)
                .map(FranchiseApiMapper::toDomain)
                .flatMap(useCase::createFranchise)
                .map(FranchiseApiMapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        return request.bodyToMono(UpdateFranchiseNameRequest.class)
                .flatMap(validator::validate)
                .map(FranchiseApiMapper::toDomain)
                .flatMap(useCase::updateFranchiseName)
                .map(FranchiseApiMapper::toUpdateNameResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                );
    }
}
