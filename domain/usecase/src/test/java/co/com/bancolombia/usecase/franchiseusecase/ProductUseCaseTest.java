package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    private UUID branchId;
    private UUID productId;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        branchId = UUID.randomUUID();
        productId = UUID.randomUUID();
        branch = Branch.create("Main Branch");
        product = Product.create("Laptop", 10);
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void shouldAddProductToBranchSuccessfully() {
        // Arrange
        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));
        when(productRepository.addProductToBranch(any(UUID.class), any(Product.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.execute(branchId, "Laptop", 10);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(productRepository).addProductToBranch(eq(branchId), any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when branch not found while adding product")
    void shouldThrowExceptionWhenBranchNotFoundWhileAddingProduct() {
        // Arrange
        when(branchRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.execute(branchId, "Laptop", 10);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("Branch not found"))
                .verify();

        verify(branchRepository).findById(branchId);
        verify(productRepository, never()).addProductToBranch(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when creating product with invalid data")
    void shouldThrowExceptionWhenCreatingProductWithInvalidData() {
        // Arrange
        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        // Act
        Mono<Void> result = productUseCase.execute(branchId, null, 10);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(productRepository, never()).addProductToBranch(any(), any());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        // Arrange
        Branch branchWithId = Branch.restore(branchId, "Main Branch");
        Product productToDelete = Product.restore(productId, "Laptop", 10);
        branchWithId.addProduct(productToDelete);

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchWithId));
        when(productRepository.deleteById(productId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.execute(branchId, productId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("Should throw exception when branch not found while deleting product")
    void shouldThrowExceptionWhenBranchNotFoundWhileDeletingProduct() {
        // Arrange
        when(branchRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.execute(branchId, productId);

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update product stock successfully")
    void shouldUpdateProductStockSuccessfully() {
        // Arrange
        Product updatedProduct = Product.restore(productId, "Laptop", 20);

        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(product));
        when(productRepository.updateProduct(productId, 20))
                .thenReturn(Mono.just(updatedProduct));

        // Act
        Mono<Product> result = productUseCase.execute(productId, 20);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStock() == 20)
                .verifyComplete();

        verify(productRepository).findById(productId.toString());
        verify(productRepository).updateProduct(productId, 20);
    }

    @Test
    @DisplayName("Should throw exception when product not found while updating stock")
    void shouldThrowExceptionWhenProductNotFoundWhileUpdatingStock() {
        // Arrange
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        // Act
        Mono<Product> result = productUseCase.execute(productId, 20);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(productRepository).findById(productId.toString());
        verify(productRepository, never()).updateProduct(any(UUID.class), anyInt());
    }

    @Test
    @DisplayName("Should throw exception when updating stock with negative value")
    void shouldThrowExceptionWhenUpdatingStockWithNegativeValue() {
        // Arrange
        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(product));

        // Act
        Mono<Product> result = productUseCase.execute(productId, -5);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(productRepository).findById(productId.toString());
        verify(productRepository, never()).updateProduct(any(UUID.class), anyInt());
    }

    @Test
    @DisplayName("Should update product name successfully")
    void shouldUpdateProductNameSuccessfully() {
        // Arrange
        Product productWithId = Product.restore(productId, "Laptop", 10);
        Product updatedProduct = Product.restore(productId, "New Laptop", 10);

        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(productWithId));
        when(productRepository.updateProduct(any(Product.class)))
                .thenReturn(Mono.just(updatedProduct));

        // Act
        Mono<Product> result = productUseCase.updateProductName(productId, "New Laptop");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(p -> p.getName().equals("New Laptop"))
                .verifyComplete();

        verify(productRepository).findById(productId.toString());
        verify(productRepository).updateProduct(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when product not found while updating name")
    void shouldThrowExceptionWhenProductNotFoundWhileUpdatingName() {
        // Arrange
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        // Act
        Mono<Product> result = productUseCase.updateProductName(productId, "New Name");

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(productRepository).findById(productId.toString());
        verify(productRepository, never()).updateProduct(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when updating product name with null")
    void shouldThrowExceptionWhenUpdatingProductNameWithNull() {
        // Arrange
        Product productWithId = Product.restore(productId, "Laptop", 10);

        when(productRepository.findById(productId.toString()))
                .thenReturn(Mono.just(productWithId));

        // Act
        Mono<Product> result = productUseCase.updateProductName(productId, null);

        // Assert
        StepVerifier.create(result)
                .expectError(ProductException.class)
                .verify();

        verify(productRepository).findById(productId.toString());
        verify(productRepository, never()).updateProduct(any(Product.class));
    }
}

