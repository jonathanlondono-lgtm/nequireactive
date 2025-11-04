package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchEntity, UUID> {

    Flux<BranchEntity> findByFranchiseId(UUID franchiseId);

    @Query("INSERT INTO branch (id, franchise_id, name) VALUES (:id, :franchiseId, :name)")
    Mono<Void> insertBranch(UUID id, UUID franchiseId, String name);

    @Query("SELECT * FROM branch WHERE franchise_id = $1")
    Flux<BranchEntity> findAllByFranchiseId(UUID franchiseId);
}

