package co.com.bancolombia.api.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchNameRequest {
    @NotNull(message = "branchId is required")
    private UUID branchId;

    @NotBlank(message = "newName is required")
    private String newName;
}