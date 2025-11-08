package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.FranchiseDTO;
import co.com.bancolombia.api.dto.request.UpdateFranchiseNameRequest;
import co.com.bancolombia.api.dto.response.UpdateFranchiseNameResponse;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchiseusecase.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase useCase;
    private final RequestValidator validator;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(validator::validate)
                .map(dto -> Franchise.builder()
                        .name(dto.getName())
                        .branches(new ArrayList<>())
                        .build())
                .flatMap(useCase::createFranchise)
                .flatMap(franchise -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchise));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        return request.bodyToMono(UpdateFranchiseNameRequest.class)
                .flatMap(validator::validate)
                .map(dto -> Franchise.builder()
                        .id(dto.getFranchiseId())
                        .name(dto.getNewName())
                        .branches(new ArrayList<>())
                        .build())
                .flatMap(useCase::updateFranchiseName)
                .map(franchise -> new UpdateFranchiseNameResponse(franchise.getId(), franchise.getName()))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                );
    }
}
