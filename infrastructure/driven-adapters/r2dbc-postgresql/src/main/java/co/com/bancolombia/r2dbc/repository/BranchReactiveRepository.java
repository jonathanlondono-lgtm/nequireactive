package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchEntity, UUID> {
}

