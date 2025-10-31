package co.com.bancolombia.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductRequest {

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be non-negative")
    private Integer stock;
}

