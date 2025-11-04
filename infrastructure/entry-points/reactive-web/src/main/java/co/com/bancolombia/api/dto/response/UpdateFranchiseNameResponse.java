package co.com.bancolombia.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFranchiseNameResponse {
    private UUID franchiseId;
    private String name;
}