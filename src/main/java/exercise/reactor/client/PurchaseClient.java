package exercise.reactor.client;

import exercise.reactor.dto.PurchaseList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PurchaseClient {
    @Autowired
    private WebClient webClient;

    public Mono<PurchaseList> listByUsername(String username, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("purchases/by_user/" + username)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(PurchaseList.class);
    }

    public Mono<PurchaseList> listByProductId(int productId, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("purchases/by_product/" + productId)
                        .queryParam("limit", limit).build())
                .retrieve()
                .bodyToMono(PurchaseList.class)
                .doOnError(e->log.error(e.getMessage()));
    }
}
