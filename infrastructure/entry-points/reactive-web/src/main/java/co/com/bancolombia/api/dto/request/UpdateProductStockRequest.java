package co.com.bancolombia.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductStockRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "New stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer newStock;
}

