package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.Franchise;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(UUID id);
    Mono<Franchise> update(Franchise franchise);
}
