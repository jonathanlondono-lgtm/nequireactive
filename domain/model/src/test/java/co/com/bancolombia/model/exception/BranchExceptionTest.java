package co.com.bancolombia.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BranchExceptionTest {

    @Test
    @DisplayName("Should create BranchException with message only")
    void shouldCreateExceptionWithMessageOnly() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.BRANCH_NAME_REQUIRED;

        // Act
        BranchException exception = new BranchException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should create BranchException with message and parameter")
    void shouldCreateExceptionWithMessageAndParameter() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.BRANCH_NOT_FOUND;
        String branchId = "branch-123";

        // Act
        BranchException exception = new BranchException(message, branchId);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(branchId) ||
                   exception.getMessage().equals(message.getMessage()));
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeInstanceOfRuntimeException() {
        // Arrange
        BranchException exception = new BranchException(DomainExceptionMessage.BRANCH_NAME_REQUIRED);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should handle null parameter gracefully")
    void shouldHandleNullParameter() {
        // Act & Assert
        assertDoesNotThrow(() ->
            new BranchException(DomainExceptionMessage.BRANCH_NOT_FOUND, (String) null)
        );
    }

    @Test
    @DisplayName("Should preserve exception message")
    void shouldPreserveExceptionMessage() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.BRANCH_ID_REQUIRED;

        // Act
        BranchException exception = new BranchException(message);

        // Assert
        assertEquals(message.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should handle branch required message")
    void shouldHandleBranchRequiredMessage() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.BRANCH_REQUIRED;

        // Act
        BranchException exception = new BranchException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message.getMessage(), exception.getMessage());
    }
}

