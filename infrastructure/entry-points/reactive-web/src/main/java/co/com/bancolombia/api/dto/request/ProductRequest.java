package co.com.bancolombia.api.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private UUID branchId;

    @NotBlank(message = "product name is required")
    private String name;

    @Min(value = 0, message = "stock must be non-negative")
    private int stock;
}
