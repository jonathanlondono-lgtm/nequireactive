package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ProductAdapter extends ReactiveAdapterOperations<Product, ProductEntity, String, ProductReactiveRepository>
implements ProductRepository
{
    public ProductAdapter(ProductReactiveRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return repository
                .save(mapper.map(product, ProductEntity.class))
                .map(entity -> mapper.map(entity, Product.class));
           }

    @Override
    public Mono<Product> updateProduct(Product product) {
        return null;
    }

    @Override
    public Mono<Product> getProductByName(String name, UUID branchId) {
        return null;
    }

    @Override
    public Mono<Product> getProductById(UUID id) {
        return null;
    }

    @Override
    public Mono<Void> removeProduct(Product product) {
        return null;
    }

    @Override
    public Flux<Product> getTopProductsForBranchAndFranchise(UUID franchise) {
        return null;
    }
}
