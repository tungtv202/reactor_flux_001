package exercise.reactor.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("product")
public class Product {
    private Integer id;
    private String face;
    private Integer price;
    private Integer size;
}
