package co.com.bancolombia.model.branch;
import co.com.bancolombia.model.product.Product;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Branch {
    private UUID id;
    private String name;
    private UUID franchiseId;
    private List<Product> products;
}