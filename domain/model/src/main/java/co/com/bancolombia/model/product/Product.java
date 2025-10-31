package co.com.bancolombia.model.product;


import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.ProductException;

import java.util.UUID;

public class Product {

    private final String id;
    private String name;
    private int stock;

    private Product(String name, int stock) {
        if (name == null || name.trim().isEmpty()) {
            throw new ProductException(DomainExceptionMessage.PRODUCT_NAME_REQUIRED);
        }
        if (stock < 0) {
            throw new ProductException(DomainExceptionMessage.PRODUCT_STOCK_NON_NEGATIVE);
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.stock = stock;
    }

    public static Product create(String name, int stock) {
        return new Product(name, stock);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void rename(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new ProductException(DomainExceptionMessage.PRODUCT_NAME_REQUIRED);
        }
        this.name = newName;
    }

    public void updateStock(int newStock) {
        if (newStock < 0) {
            throw new ProductException(DomainExceptionMessage.PRODUCT_STOCK_NON_NEGATIVE);
        }
        this.stock = newStock;
    }
}
