package co.com.bancolombia.model.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should create product with builder")
    void shouldCreateProductWithBuilder() {
        // Arrange & Act
        Product product = Product.builder()
                .name("Laptop")
                .stock(10)
                .branchId(UUID.randomUUID())
                .build();

        // Assert
        assertNotNull(product);
        assertEquals("Laptop", product.getName());
        assertEquals(10, product.getStock());
        assertNotNull(product.getBranchId());
    }

    @Test
    @DisplayName("Should create product with id")
    void shouldCreateProductWithId() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        // Act
        Product product = Product.builder()
                .id(id)
                .name("Mouse")
                .stock(20)
                .branchId(branchId)
                .build();

        // Assert
        assertEquals(id, product.getId());
        assertEquals("Mouse", product.getName());
        assertEquals(20, product.getStock());
        assertEquals(branchId, product.getBranchId());
    }

    @Test
    @DisplayName("Should allow modifying product name")
    void shouldAllowModifyingProductName() {
        // Arrange
        Product product = Product.builder()
                .name("Old Name")
                .stock(5)
                .branchId(UUID.randomUUID())
                .build();

        // Act
        product.setName("New Name");

        // Assert
        assertEquals("New Name", product.getName());
    }

    @Test
    @DisplayName("Should allow modifying product stock")
    void shouldAllowModifyingProductStock() {
        // Arrange
        Product product = Product.builder()
                .name("Product")
                .stock(10)
                .branchId(UUID.randomUUID())
                .build();

        // Act
        product.setStock(25);

        // Assert
        assertEquals(25, product.getStock());
    }

    @Test
    @DisplayName("Should allow zero stock")
    void shouldAllowZeroStock() {
        // Arrange & Act
        Product product = Product.builder()
                .name("Product")
                .stock(0)
                .branchId(UUID.randomUUID())
                .build();

        // Assert
        assertEquals(0, product.getStock());
    }
}

