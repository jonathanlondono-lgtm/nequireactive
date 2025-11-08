package co.com.bancolombia.model.product;
import co.com.bancolombia.model.branch.Branch;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private UUID id;
    private String name;
    private Integer stock;
    private UUID branchId;
    private Branch branch;
}