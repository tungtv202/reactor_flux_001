package exercise.reactor;


import exercise.reactor.dto.PopularPurchasesDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CacheService {

    List<PopularPurchasesDto> getPopularPurchases(String username);

    Mono<List<PopularPurchasesDto>> getPopularPurchasesAsync(String username);

    void setPopularPurchases(String username, Flux<PopularPurchasesDto> value);

    void setPopularPurchases(String username, List<PopularPurchasesDto> value);

}
