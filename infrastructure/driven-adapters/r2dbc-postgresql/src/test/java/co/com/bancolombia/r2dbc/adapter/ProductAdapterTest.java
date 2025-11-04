package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    private ProductReactiveRepository productRepository;

    @InjectMocks
    private ProductAdapter productAdapter;

    private UUID branchId;
    private UUID productId;
    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        branchId = UUID.randomUUID();
        productId = UUID.randomUUID();
        product = Product.create("Laptop", 10);
        productEntity = ProductEntity.builder()
                .id(productId)
                .branchId(branchId)
                .name("Laptop")
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void shouldAddProductToBranchSuccessfully() {
        // Arrange
        when(productRepository.insertProduct(any(UUID.class), any(UUID.class), anyString(), anyInt()))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.addProductToBranch(branchId, product);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).insertProduct(any(UUID.class), eq(branchId), eq("Laptop"), eq(10));
    }

    @Test
    @DisplayName("Should find product by id successfully")
    void shouldFindProductByIdSuccessfully() {
        // Arrange
        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(productEntity));

        // Act
        Mono<Product> result = productAdapter.findById(productId.toString());

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop") && p.getStock() == 10)
                .verifyComplete();

        verify(productRepository).findById(productId.toString());
    }

    @Test
    @DisplayName("Should return empty when product not found")
    void shouldReturnEmptyWhenProductNotFound() {
        // Arrange
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        // Act
        Mono<Product> result = productAdapter.findById(UUID.randomUUID().toString());

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(anyString());
    }

    @Test
    @DisplayName("Should delete product by id successfully")
    void shouldDeleteProductByIdSuccessfully() {
        // Arrange
        when(productRepository.deleteById(productId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.deleteById(productId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("Should update product stock successfully")
    void shouldUpdateProductStockSuccessfully() {
        // Arrange
        ProductEntity updatedEntity = ProductEntity.builder()
                .id(productId)
                .branchId(branchId)
                .name("Laptop")
                .stock(20)
                .build();

        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(productEntity));
        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        // Act
        Mono<Product> result = productAdapter.updateProduct(productId, 20);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStock() == 20)
                .verifyComplete();

        verify(productRepository).findById(productId.toString());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should update product name successfully")
    void shouldUpdateProductNameSuccessfully() {
        // Arrange
        Product productToUpdate = Product.restore(productId, "New Laptop", 10);

        when(productRepository.updateProductName(anyString(), any(UUID.class)))
                .thenReturn(Mono.just(1));

        // Act
        Mono<Product> result = productAdapter.updateProduct(productToUpdate);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("New Laptop"))
                .verifyComplete();

        verify(productRepository).updateProductName("New Laptop", productId);
    }

    @Test
    @DisplayName("Should find product with highest stock by branch id")
    void shouldFindProductWithHighestStockByBranchId() {
        // Arrange
        when(productRepository.findTopByBranchIdOrderByStockDesc(branchId))
                .thenReturn(Mono.just(productEntity));

        // Act
        Mono<Product> result = productAdapter.findProductWithHighestStockByBranchId(branchId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Laptop") && p.getStock() == 10)
                .verifyComplete();

        verify(productRepository).findTopByBranchIdOrderByStockDesc(branchId);
    }

    @Test
    @DisplayName("Should return empty when no products in branch")
    void shouldReturnEmptyWhenNoProductsInBranch() {
        // Arrange
        when(productRepository.findTopByBranchIdOrderByStockDesc(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Product> result = productAdapter.findProductWithHighestStockByBranchId(branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findTopByBranchIdOrderByStockDesc(branchId);
    }
}

