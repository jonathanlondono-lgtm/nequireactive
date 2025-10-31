package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.BranchDTO;
import co.com.bancolombia.usecase.franchiseusecase.BranchUseCase;
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

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        UUID franchiseId = UUID.fromString(request.pathVariable("id"));
        return request.bodyToMono(BranchDTO.class)
                .flatMap(body -> useCase.execute(franchiseId, body.getName()))
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }
}
