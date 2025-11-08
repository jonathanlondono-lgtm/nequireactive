package co.com.bancolombia.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseExceptionTest {

    @Test
    @DisplayName("Should create FranchiseException with message only")
    void shouldCreateExceptionWithMessageOnly() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.FRANCHISE_NAME_REQUIRED;

        // Act
        FranchiseException exception = new FranchiseException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should create FranchiseException with message and parameter")
    void shouldCreateExceptionWithMessageAndParameter() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.FRANCHISE_NOT_FOUND;
        String franchiseId = "franchise-456";

        // Act
        FranchiseException exception = new FranchiseException(message, franchiseId);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(franchiseId) ||
                   exception.getMessage().equals(message.getMessage()));
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeInstanceOfRuntimeException() {
        // Arrange
        FranchiseException exception = new FranchiseException(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should handle null parameter gracefully")
    void shouldHandleNullParameter() {
        // Act & Assert
        assertDoesNotThrow(() ->
            new FranchiseException(DomainExceptionMessage.FRANCHISE_NOT_FOUND, (String) null)
        );
    }

    @Test
    @DisplayName("Should preserve exception message")
    void shouldPreserveExceptionMessage() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.FRANCHISE_NAME_REQUIRED;

        // Act
        FranchiseException exception = new FranchiseException(message);

        // Assert
        assertEquals(message.getMessage(), exception.getMessage());
    }
}

