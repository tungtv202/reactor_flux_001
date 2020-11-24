package exercise.reactor;


import exercise.reactor.dto.PopularPurchasesDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CacheService {

    List<PopularPurchasesDto> getPopularPurchases(String username);

    void setPopularPurchases(String username, Flux<PopularPurchasesDto> value);
}
