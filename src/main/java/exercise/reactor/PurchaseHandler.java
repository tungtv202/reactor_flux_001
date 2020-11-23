package exercise.reactor;

import exercise.reactor.client.ProductClient;
import exercise.reactor.client.PurchaseClient;
import exercise.reactor.client.UserClient;
import exercise.reactor.dto.PopularPurchasesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserClient userClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private PurchaseClient purchaseClient;

    public Mono<ServerResponse> purchasesRecent(ServerRequest request) {
        final var username = request.pathVariable("username");
        return userClient.get(username)
                .flatMap(c -> ok().contentType(MediaType.APPLICATION_JSON)
                        .body(getPopularPurchasesDtoFlux(username), PopularPurchasesDto.class))
                .switchIfEmpty(ok().bodyValue(String.format("User with username of %s  was not found", username)))
                ;
    }

    private Flux<PopularPurchasesDto> getPopularPurchasesDtoFlux(String username) {
//        if (1==1) return Flux.error(new RuntimeException("1233"));
        var purchaseFlux = purchaseClient.listByUsername(username, PURCHASES_RECENT_LIMIT).flatMapMany(Flux::fromIterable);
        var productFlux = purchaseFlux.flatMap(e -> productClient.get(e.getProductId()));
        var result = productFlux.flatMap(e2 -> purchaseClient.listByProductId(e2.getId(), Integer.MAX_VALUE)
                .map(r -> new PopularPurchasesDto(e2, r)));
        return result.collectSortedList((a, b) -> a.getRecentUsername().size() > b.getRecentUsername().size() ? -1 : 0).flatMapMany(Flux::fromIterable);
    }
}
