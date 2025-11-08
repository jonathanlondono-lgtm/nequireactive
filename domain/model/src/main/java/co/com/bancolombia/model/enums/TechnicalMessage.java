package co.com.bancolombia.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    // Errores generales (5xx)
    INTERNAL_ERROR("500", "Something went wrong, please try again", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Something went wrong in adapters, please try again", ""),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),
    DATABASE_ERROR("PRC502", "Database error, please try again", ""),

    // Errores de validación (4xx)
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS("400", "Bad Parameters, please verify data", ""),

    // Errores de negocio - Franchise
    FRANCHISE_NOT_FOUND("404", "Franchise not found: %s", "franchiseId"),
    FRANCHISE_NAME_REQUIRED("400", "Franchise name is required", "name"),
    FRANCHISE_ALREADY_EXISTS("409", "Franchise already exists", "name"),

    // Errores de negocio - Branch
    BRANCH_NOT_FOUND("404", "Branch not found: %s", "branchId"),
    BRANCH_NAME_REQUIRED("400", "Branch name is required", "name"),
    BRANCH_ALREADY_EXISTS("409", "Branch already exists in this franchise", "name"),
    BRANCH_FRANCHISE_REQUIRED("400", "Franchise ID is required for branch", "franchiseId"),

    // Errores de negocio - Product
    PRODUCT_NOT_FOUND("404", "Product not found: %s", "productId"),
    PRODUCT_NAME_REQUIRED("400", "Product name is required", "name"),
    PRODUCT_STOCK_NON_NEGATIVE("400", "Stock must be non-negative", "stock"),
    PRODUCT_BRANCH_REQUIRED("400", "Branch ID is required for product", "branchId"),
    PRODUCT_ALREADY_EXISTS("409", "Product already exists in this branch", "name"),

    // Éxitos (2xx)
    FRANCHISE_CREATED("201", "Franchise created successfully", ""),
    BRANCH_CREATED("201", "Branch created successfully", ""),
    PRODUCT_CREATED("201", "Product created successfully", ""),
    RESOURCE_UPDATED("200", "Resource updated successfully", ""),
    RESOURCE_DELETED("204", "Resource deleted successfully", "");

    private final String code;
    private final String message;
    private final String param;
}

