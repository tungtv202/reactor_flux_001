package exercise.reactor.client;

import exercise.reactor.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ProductClient {
    @Autowired
    private WebClient webClient;

    public Mono<Product> get(int id) {
        return webClient.get()
                .uri("products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(e -> log.error(e.getMessage()));
    }
}
