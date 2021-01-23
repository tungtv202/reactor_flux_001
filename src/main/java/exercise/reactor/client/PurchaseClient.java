package exercise.reactor.client;

import exercise.reactor.dto.PurchaseList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class PurchaseClient {
    @Autowired
    private WebClient webClient;

    public Mono<PurchaseList> listByUsername(String username, int limit) {
        log.info("Start listByUsername, username={}", username);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("purchases/by_user/" + username)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(PurchaseList.class)
                .doOnNext(e -> log.info("PurchaseList username={}, data={}", username, e))
                .delayElement(Duration.ofMillis(1000))
                .doOnError(e -> log.error(e.getMessage()));
    }

    public Mono<PurchaseList> listByProductId(int productId, int limit) {
        log.info("Start listByProductId, id={}", productId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("purchases/by_product/" + productId)
                        .queryParam("limit", limit).build())
                .retrieve()
                .bodyToMono(PurchaseList.class)
                .doOnNext(e -> log.info("listByProductId productId={}, data={}", productId, e))
                .delayElement(Duration.ofMillis(600))
                .doOnError(e -> log.error(e.getMessage()));
    }
}
