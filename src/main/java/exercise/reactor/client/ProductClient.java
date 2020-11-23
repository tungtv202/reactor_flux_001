package exercise.reactor.client;

import exercise.reactor.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
    @Autowired
    private WebClient webClient;

    public Mono<Product> get(int id) {
        return webClient.get()
                .uri("products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
