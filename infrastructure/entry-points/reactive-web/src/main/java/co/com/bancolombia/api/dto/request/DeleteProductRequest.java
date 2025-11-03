package co.com.bancolombia.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DeleteProductRequest {
    @NotNull(message = "branchId is required")
    private UUID branchId;

    @NotNull(message = "productId is required")
    private UUID productId;
}