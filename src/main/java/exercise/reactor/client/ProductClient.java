package exercise.reactor.client;

import exercise.reactor.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class ProductClient {
    @Autowired
    private WebClient webClient;

    public Mono<Product> get(int id) {
        log.info("Start get productId={}", id);
        return webClient.get()
                .uri("products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(e -> log.info("Product id={}, data={}", id, e))
                .delayElement(Duration.ofMillis(500))
                .doOnError(e -> log.error(e.getMessage()));
    }
}
