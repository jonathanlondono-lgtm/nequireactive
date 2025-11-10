package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    private UUID franchiseId;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        franchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC")
                .branches(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void shouldCreateFranchiseSuccessfully() {
        Franchise newFranchise = Franchise.builder()
                .name("McDonalds")
                .branches(new ArrayList<>())
                .build();

        Franchise savedFranchise = Franchise.builder()
                .id(UUID.randomUUID())
                .name("McDonalds")
                .branches(new ArrayList<>())
                .build();

        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(savedFranchise));

        Mono<Franchise> result = franchiseUseCase.createFranchise(newFranchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("McDonalds") && f.getId() != null)
                .verifyComplete();

        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Should update franchise name successfully")
    void shouldUpdateFranchiseNameSuccessfully() {
        Franchise updatedFranchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC Premium")
                .branches(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseRepository.update(any(Franchise.class)))
                .thenReturn(Mono.just(updatedFranchise));

        Mono<Franchise> result = franchiseUseCase.updateFranchiseName(updatedFranchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC Premium"))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).update(any(Franchise.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent franchise")
    void shouldThrowExceptionWhenUpdatingNonExistentFranchise() {
        Franchise updatedFranchise = Franchise.builder()
                .id(franchiseId)
                .name("New Name")
                .branches(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        Mono<Franchise> result = franchiseUseCase.updateFranchiseName(updatedFranchise);

        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository, never()).update(any());
    }
}

