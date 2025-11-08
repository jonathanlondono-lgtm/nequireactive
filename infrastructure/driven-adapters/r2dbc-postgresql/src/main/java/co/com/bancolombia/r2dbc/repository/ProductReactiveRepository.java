package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, UUID> {

    @Query("""
            WITH max_stock_per_branch AS (
                SELECT b.id as branch_id, MAX(p.stock) as max_stock
                FROM branch b
                LEFT JOIN product p ON b.id = p.branch_id
                WHERE b.franchise_id = :franchiseId
                GROUP BY b.id
            )
            SELECT p.id, p.branch_id, p.name, p.stock, p.created_at, p.updated_at
            FROM product p
            INNER JOIN max_stock_per_branch m ON p.branch_id = m.branch_id AND p.stock = m.max_stock
            ORDER BY p.branch_id
            """)
    Flux<ProductEntity> findProductsWithHighestStockByFranchiseId(UUID franchiseId);
}

