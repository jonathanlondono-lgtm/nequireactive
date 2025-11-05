package co.com.bancolombia.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FranchiseDTO {
    @NotBlank(message = "Franchise name is required")
    private String name;
}