package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, String>, ReactiveQueryByExampleExecutor<ProductEntity> {
    @Query("INSERT INTO product (id, branch_id, name, stock) VALUES ($1, $2, $3, $4)")
    Mono<Void> insertProduct(UUID id, UUID branchId, String name, int stock);

    @Query("DELETE FROM product WHERE id = $1")
    Mono<Void> deleteById(UUID id);

}
