package co.com.bancolombia.model.enums;

public enum DomainExceptionMessage implements IExceptionMessage {
    PRODUCT_NAME_REQUIRED("Product name is required"),
    PRODUCT_STOCK_NON_NEGATIVE("Stock must be non-negative"),
    PRODUCT_REQUIRED("Product is required"),
    PRODUCT_NOT_FOUND("Product not found: %s"),

    BRANCH_NAME_REQUIRED("Branch name is required"),
    BRANCH_ID_REQUIRED("Branch ID is required"),
    BRANCH_REQUIRED("Branch is required"),
    BRANCH_NOT_FOUND("Branch not found: %s"),

    FRANCHISE_NAME_REQUIRED("Franchise name is required"),
    FRANCHISE_REQUIRED("Franchise is required"),
    FRANCHISE_NOT_FOUND("Franchise not found: %s");

    private final String message;

    DomainExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

