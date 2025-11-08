package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.product.ProductUseCase;
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

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    private UUID franchiseId;
    private UUID branchId;
    private UUID productId;
    private Branch branch;
    private Franchise franchise;
    private Product product;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        productId = UUID.randomUUID();

        franchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC")
                .branches(new ArrayList<>())
                .build();

        branch = Branch.builder()
                .id(branchId)
                .name("Main Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        product = Product.builder()
                .id(productId)
                .name("Laptop")
                .stock(10)
                .branchId(branchId)
                .build();
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void shouldAddProductToBranchSuccessfully() {
        // Arrange
        Product newProduct = Product.builder()
                .name("Mouse")
                .stock(50)
                .branchId(branchId)
                .build();

        Product savedProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Mouse")
                .stock(50)
                .branchId(branchId)
                .build();

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));
        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.just(savedProduct));

        // Act
        Mono<Product> result = productUseCase.addProductToBranch(newProduct);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Mouse") && p.getStock() == 50)
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when branch not found while adding product")
    void shouldThrowExceptionWhenBranchNotFoundWhileAddingProduct() {
        // Arrange
        Product newProduct = Product.builder()
                .name("Mouse")
                .stock(50)
                .branchId(branchId)
                .build();

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Product> result = productUseCase.addProductToBranch(newProduct);

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        // Arrange
        when(productRepository.findById(productId))
                .thenReturn(Mono.just(product));
        when(productRepository.deleteById(productId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.deleteProduct(productId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("Should throw exception when product not found while deleting")
    void shouldThrowExceptionWhenProductNotFoundWhileDeleting() {
        // Arrange
        when(productRepository.findById(productId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.deleteProduct(productId);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(productRepository).findById(productId);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update product stock successfully")
    void shouldUpdateProductStockSuccessfully() {
        // Arrange
        Product updatedProduct = Product.builder()
                .id(productId)
                .stock(100)
                .build();

        Product savedProduct = Product.builder()
                .id(productId)
                .name("Laptop")
                .stock(100)
                .branchId(branchId)
                .build();

        when(productRepository.findById(productId))
                .thenReturn(Mono.just(product));
        when(productRepository.update(any(Product.class)))
                .thenReturn(Mono.just(savedProduct));

        // Act
        Mono<Product> result = productUseCase.updateProductStock(updatedProduct);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStock() == 100)
                .verifyComplete();

        verify(productRepository).findById(productId);
        verify(productRepository).update(any(Product.class));
    }

    @Test
    @DisplayName("Should update product name successfully")
    void shouldUpdateProductNameSuccessfully() {
        // Arrange
        Product updatedProduct = Product.builder()
                .id(productId)
                .name("Gaming Laptop")
                .build();

        Product savedProduct = Product.builder()
                .id(productId)
                .name("Gaming Laptop")
                .stock(10)
                .branchId(branchId)
                .build();

        when(productRepository.findById(productId))
                .thenReturn(Mono.just(product));
        when(productRepository.update(any(Product.class)))
                .thenReturn(Mono.just(savedProduct));

        // Act
        Mono<Product> result = productUseCase.updateProductName(updatedProduct);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Gaming Laptop"))
                .verifyComplete();

        verify(productRepository).findById(productId);
        verify(productRepository).update(any(Product.class));
    }

    @Test
    @DisplayName("Should get products with highest stock by franchise")
    void shouldGetProductsWithHighestStockByFranchise() {
        // Arrange
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .stock(50)
                .branchId(branchId)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .stock(30)
                .branchId(UUID.randomUUID())
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(productRepository.findProductsWithHighestStockByFranchiseId(franchiseId))
                .thenReturn(Flux.just(product1, product2));

        // Act
        Flux<Product> result = productUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("Product 1") && p.getStock() == 50)
                .expectNextMatches(p -> p.getName().equals("Product 2") && p.getStock() == 30)
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(productRepository).findProductsWithHighestStockByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Should throw exception when franchise not found")
    void shouldThrowExceptionWhenFranchiseNotFound() {
        // Arrange
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // Act
        Flux<Product> result = productUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(franchiseRepository).findById(franchiseId);
        verify(productRepository, never()).findProductsWithHighestStockByFranchiseId(any());
    }
}

