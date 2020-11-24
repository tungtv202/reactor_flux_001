package exercise.reactor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularPurchasesDto {
    @JsonProperty("id")
    private Integer productId;
    @JsonProperty("face")
    private String productFace;
    @JsonProperty("price")
    private Integer productPrice;
    @JsonProperty("size")
    private Integer productSize;
    @JsonProperty("recent")
    List<String> recentUsername;

    public PopularPurchasesDto(Product product, List<Purchase> purchases) {
        this.productId = product.getId();
        this.productFace = product.getFace();
        this.productPrice = product.getPrice();
        this.productSize = product.getSize();
        this.recentUsername = purchases == null ? new ArrayList<>() :
                purchases.stream()
                        .map(Purchase::getUsername)
                        .distinct()
                        .collect(Collectors.toList());
    }
}
