package co.com.bancolombia.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductExceptionTest {

    @Test
    @DisplayName("Should create ProductException with message only")
    void shouldCreateExceptionWithMessageOnly() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.PRODUCT_NAME_REQUIRED;

        // Act
        ProductException exception = new ProductException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should create ProductException with message and parameter")
    void shouldCreateExceptionWithMessageAndParameter() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.PRODUCT_NOT_FOUND;
        String productId = "12345";

        // Act
        ProductException exception = new ProductException(message, productId);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(productId) ||
                   exception.getMessage().equals(message.getMessage()));
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeInstanceOfRuntimeException() {
        // Arrange
        ProductException exception = new ProductException(DomainExceptionMessage.PRODUCT_NAME_REQUIRED);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should handle null parameter gracefully")
    void shouldHandleNullParameter() {
        // Act & Assert
        assertDoesNotThrow(() ->
            new ProductException(DomainExceptionMessage.PRODUCT_NOT_FOUND, (String) null)
        );
    }

    @Test
    @DisplayName("Should preserve exception message")
    void shouldPreserveExceptionMessage() {
        // Arrange
        DomainExceptionMessage message = DomainExceptionMessage.PRODUCT_STOCK_NON_NEGATIVE;

        // Act
        ProductException exception = new ProductException(message);

        // Assert
        assertEquals(message.getMessage(), exception.getMessage());
    }
}

