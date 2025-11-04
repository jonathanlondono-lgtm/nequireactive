package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.dto.MaxStockByBranchResponse;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    private UUID franchiseId;
    private UUID branchId;
    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        franchise = Franchise.create("KFC");
        branch = Branch.create("Main Branch");
        product = Product.create("Laptop", 50);
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void shouldCreateFranchiseSuccessfully() {
        // Arrange
        when(franchiseRepository.saveFranchise(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // Act
        Mono<Franchise> result = franchiseUseCase.createFranchise("KFC");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC"))
                .verifyComplete();

        verify(franchiseRepository).saveFranchise(any(Franchise.class));
    }

    @Test
    @DisplayName("Should throw exception when creating franchise with null name")
    void shouldThrowExceptionWhenCreatingFranchiseWithNullName() {
        // Act & Assert
        StepVerifier.create(
                Mono.defer(() -> {
                    try {
                        return franchiseUseCase.createFranchise(null);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
        )
        .expectError(FranchiseException.class)
        .verify();

        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    @DisplayName("Should get products with highest stock by franchise")
    void shouldGetProductsWithHighestStockByFranchise() {
        // Arrange
        Franchise franchiseWithId = Franchise.restore(franchiseId, "KFC");
        Branch branchWithId = Branch.restore(branchId, "Main Branch");

        when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(franchiseWithId));
        when(branchRepository.findAllByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchWithId));
        when(productRepository.findProductWithHighestStockByBranchId(branchId))
                .thenReturn(Mono.just(product));

        // Act
        Flux<MaxStockByBranchResponse> result = franchiseUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getBranchId().equals(branchId) &&
                        response.getBranchName().equals("Main Branch") &&
                        response.getProductName().equals("Laptop") &&
                        response.getStock() == 50
                )
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(branchRepository).findAllByFranchiseId(franchiseId);
        verify(productRepository).findProductWithHighestStockByBranchId(branchId);
    }

    @Test
    @DisplayName("Should throw exception when franchise not found")
    void shouldThrowExceptionWhenFranchiseNotFound() {
        // Arrange
        when(franchiseRepository.getFranchiseById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Flux<MaxStockByBranchResponse> result = franchiseUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().contains("Franchise not found"))
                .verify();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(branchRepository, never()).findAllByFranchiseId(any());
    }

    @Test
    @DisplayName("Should return empty flux when franchise has no branches")
    void shouldReturnEmptyFluxWhenFranchiseHasNoBranches() {
        // Arrange
        Franchise franchiseWithId = Franchise.restore(franchiseId, "KFC");

        when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(franchiseWithId));
        when(branchRepository.findAllByFranchiseId(franchiseId))
                .thenReturn(Flux.empty());

        // Act
        Flux<MaxStockByBranchResponse> result = franchiseUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(branchRepository).findAllByFranchiseId(franchiseId);
        verify(productRepository, never()).findProductWithHighestStockByBranchId(any());
    }

    @Test
    @DisplayName("Should skip branches without products")
    void shouldSkipBranchesWithoutProducts() {
        // Arrange
        Franchise franchiseWithId = Franchise.restore(franchiseId, "KFC");
        Branch branchWithId = Branch.restore(branchId, "Empty Branch");

        when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(franchiseWithId));
        when(branchRepository.findAllByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchWithId));
        when(productRepository.findProductWithHighestStockByBranchId(branchId))
                .thenReturn(Mono.empty());

        // Act
        Flux<MaxStockByBranchResponse> result = franchiseUseCase.getProductsWithHighestStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(branchRepository).findAllByFranchiseId(franchiseId);
        verify(productRepository).findProductWithHighestStockByBranchId(branchId);
    }

    @Test
    @DisplayName("Should update franchise name successfully")
    void shouldUpdateFranchiseNameSuccessfully() {
        // Arrange
        Franchise franchiseWithId = Franchise.restore(franchiseId, "KFC");
        Franchise updatedFranchise = Franchise.restore(franchiseId, "KFC Updated");

        when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(franchiseWithId));
        when(franchiseRepository.updateFranchise(any(Franchise.class)))
                .thenReturn(Mono.just(updatedFranchise));

        // Act
        Mono<Franchise> result = franchiseUseCase.updateFranchiseName(franchiseId, "KFC Updated");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC Updated"))
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(franchiseRepository).updateFranchise(any(Franchise.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent franchise")
    void shouldThrowExceptionWhenUpdatingNonExistentFranchise() {
        // Arrange
        when(franchiseRepository.getFranchiseById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Franchise> result = franchiseUseCase.updateFranchiseName(franchiseId, "New Name");

        // Assert
        StepVerifier.create(result)
                .expectError(FranchiseException.class)
                .verify();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(franchiseRepository, never()).updateFranchise(any());
    }

    @Test
    @DisplayName("Should throw exception when updating with null name")
    void shouldThrowExceptionWhenUpdatingWithNullName() {
        // Arrange
        Franchise franchiseWithId = Franchise.restore(franchiseId, "KFC");

        when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(franchiseWithId));

        // Act
        Mono<Franchise> result = franchiseUseCase.updateFranchiseName(franchiseId, null);

        // Assert
        StepVerifier.create(result)
                .expectError()
                .verify();

        verify(franchiseRepository).getFranchiseById(franchiseId);
        verify(franchiseRepository, never()).updateFranchise(any());
    }
}
