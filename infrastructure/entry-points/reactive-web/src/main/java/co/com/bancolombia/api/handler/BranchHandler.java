package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.BranchDTO;
import co.com.bancolombia.api.dto.request.UpdateBranchNameRequest;
import co.com.bancolombia.api.mapper.BranchApiMapper;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BranchHandler {
    private final BranchUseCase useCase;
    private final RequestValidator validator;

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        UUID franchiseId = UUID.fromString(request.pathVariable("id"));
        return request.bodyToMono(BranchDTO.class)
                .flatMap(validator::validate)
                .map(dto -> BranchApiMapper.toDomain(dto, franchiseId))
                .flatMap(useCase::addBranchToFranchise)
                .map(BranchApiMapper::toCreateResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        return request.bodyToMono(UpdateBranchNameRequest.class)
                .flatMap(validator::validate)
                .map(BranchApiMapper::toDomain)
                .flatMap(useCase::updateBranchName)
                .map(BranchApiMapper::toUpdateNameResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }
}

