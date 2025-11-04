package co.com.bancolombia.model.product;


import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should create product with valid data")
    void shouldCreateProductWithValidData() {
        // Arrange & Act
        Product product = Product.create("Laptop", 10);

        // Assert
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(10, product.getStock());
    }

    @Test
    @DisplayName("Should throw exception when creating product with null name")
    void shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        ProductException exception = assertThrows(
                ProductException.class,
                () -> Product.create(null, 10)
        );

        assertEquals(DomainExceptionMessage.PRODUCT_NAME_REQUIRED.getMessage(),
                exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating product with empty name")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        ProductException exception = assertThrows(
                ProductException.class,
                () -> Product.create("   ", 10)
        );

        assertEquals(DomainExceptionMessage.PRODUCT_NAME_REQUIRED.getMessage(),
                exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when stock is negative")
    void shouldThrowExceptionWhenStockIsNegative() {
        // Act & Assert
        ProductException exception = assertThrows(
                ProductException.class,
                () -> Product.create("Laptop", -5)
        );

        assertEquals(DomainExceptionMessage.PRODUCT_STOCK_NON_NEGATIVE.getMessage(),
                exception.getMessage());
    }

    @Test
    @DisplayName("Should restore product with valid data")
    void shouldRestoreProductWithValidData() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Product product = Product.restore(id, "Mouse", 20);

        // Assert
        assertEquals(id, product.getId());
        assertEquals("Mouse", product.getName());
        assertEquals(20, product.getStock());
    }

    @Test
    @DisplayName("Should rename product successfully")
    void shouldRenameProductSuccessfully() {
        // Arrange
        Product product = Product.create("Old Name", 5);

        // Act
        product.rename("New Name");

        // Assert
        assertEquals("New Name", product.getName());
    }

    @Test
    @DisplayName("Should throw exception when renaming with null name")
    void shouldThrowExceptionWhenRenamingWithNull() {
        // Arrange
        Product product = Product.create("Product", 5);

        // Act & Assert
        assertThrows(ProductException.class, () -> product.rename(null));
    }

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStockSuccessfully() {
        // Arrange
        Product product = Product.create("Product", 10);

        // Act
        product.updateStock(25);

        // Assert
        assertEquals(25, product.getStock());
    }

    @Test
    @DisplayName("Should throw exception when updating stock with negative value")
    void shouldThrowExceptionWhenUpdatingStockWithNegative() {
        // Arrange
        Product product = Product.create("Product", 10);

        // Act & Assert
        ProductException exception = assertThrows(
                ProductException.class,
                () -> product.updateStock(-5)
        );

        assertEquals(DomainExceptionMessage.PRODUCT_STOCK_NON_NEGATIVE.getMessage(),
                exception.getMessage());
    }
}