package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    private ProductReactiveRepository productRepository;

    @InjectMocks
    private ProductAdapter productAdapter;

    private UUID branchId;
    private UUID franchiseId;
    private UUID productId;
    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        productId = UUID.randomUUID();

        product = Product.builder()
                .id(productId)
                .name("Laptop")
                .stock(10)
                .branchId(branchId)
                .build();

        productEntity = ProductEntity.builder()
                .id(productId)
                .branchId(branchId)
                .name("Laptop")
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Should save product successfully")
    void shouldSaveProductSuccessfully() {
        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(Mono.just(productEntity));

        Mono<Product> result = productAdapter.save(product);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop") && p.getStock() == 10)
                .verifyComplete();

        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should find product by id successfully")
    void shouldFindProductByIdSuccessfully() {
        when(productRepository.findById(productId))
                .thenReturn(Mono.just(productEntity));

        Mono<Product> result = productAdapter.findById(productId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop") && p.getStock() == 10)
                .verifyComplete();

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("Should return empty when product not found")
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        Mono<Product> result = productAdapter.findById(UUID.randomUUID());

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        Product updatedProduct = Product.builder()
                .id(productId)
                .name("Gaming Laptop")
                .stock(50)
                .branchId(branchId)
                .build();

        ProductEntity updatedEntity = ProductEntity.builder()
                .id(productId)
                .branchId(branchId)
                .name("Gaming Laptop")
                .stock(50)
                .build();

        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        Mono<Product> result = productAdapter.update(updatedProduct);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Gaming Laptop") && p.getStock() == 50)
                .verifyComplete();

        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        when(productRepository.deleteById(productId))
                .thenReturn(Mono.empty());

        Mono<Void> result = productAdapter.deleteById(productId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("Should find products with highest stock by franchise")
    void shouldFindProductsWithHighestStockByFranchise() {
        ProductEntity product1 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .branchId(branchId)
                .name("Product 1")
                .stock(50)
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .branchId(UUID.randomUUID())
                .name("Product 2")
                .stock(30)
                .build();

        when(productRepository.findProductsWithHighestStockByFranchiseId(franchiseId))
                .thenReturn(Flux.just(product1, product2));

        Flux<Product> result = productAdapter.findProductsWithHighestStockByFranchiseId(franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Product 1") && p.getStock() == 50)
                .expectNextMatches(p -> p.getName().equals("Product 2") && p.getStock() == 30)
                .verifyComplete();

        verify(productRepository).findProductsWithHighestStockByFranchiseId(franchiseId);
    }
}

