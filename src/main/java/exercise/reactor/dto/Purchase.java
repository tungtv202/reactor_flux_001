package exercise.reactor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private Integer id;
    private String username;
    private Integer productId;
    private Date date;
}
