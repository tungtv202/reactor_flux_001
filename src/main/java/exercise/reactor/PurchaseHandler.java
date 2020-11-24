package exercise.reactor;

import exercise.reactor.client.ProductClient;
import exercise.reactor.client.PurchaseClient;
import exercise.reactor.client.UserClient;
import exercise.reactor.dto.PopularPurchasesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class PurchaseHandler {
    public static final int PURCHASES_RECENT_LIMIT = 5;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final PurchaseClient purchaseClient;
    private final CacheService cacheService;

    @Autowired
    public PurchaseHandler(UserClient userClient, ProductClient productClient, PurchaseClient purchaseClient,
                           CacheService cacheService) {
        this.userClient = userClient;
        this.productClient = productClient;
        this.purchaseClient = purchaseClient;
        this.cacheService = cacheService;
    }

//    @Cacheable
    public Mono<ServerResponse> purchasesRecent(ServerRequest request) {
        final var username = request.pathVariable("username");
        return userClient.get(username)
                .flatMap(c -> ok().contentType(MediaType.APPLICATION_JSON)
                        .body(getPopularPurchasesDto(username), PopularPurchasesDto.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(String.format("User with username of %s  was not found", username)));
    }

    private Flux<PopularPurchasesDto> getPopularPurchasesDto(String username) {
        var cacheValue = cacheService.getPopularPurchases(username);
        if (cacheValue != null) return Flux.fromIterable(cacheValue);
        var productFlux = purchaseClient.listByUsername(username, PURCHASES_RECENT_LIMIT)
                .flatMapMany(Flux::fromIterable)
                .flatMap(e -> productClient.get(e.getProductId()));
        var popularPurchasesDtoFlux = productFlux.flatMap(e ->
                purchaseClient.listByProductId(e.getId(), Integer.MAX_VALUE)
                        .map(r -> new PopularPurchasesDto(e, r)));
        // sort
        var result = popularPurchasesDtoFlux.collectSortedList((a, b) ->
                a.getRecentUsername().size() > b.getRecentUsername().size() ? -1 : 0)
                .flatMapMany(Flux::fromIterable);
        cacheService.setPopularPurchases(username, result);
        return result;
    }
}
