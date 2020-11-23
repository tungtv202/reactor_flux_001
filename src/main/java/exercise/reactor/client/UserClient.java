package exercise.reactor.client;

import exercise.reactor.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {
    @Autowired
    private WebClient webClient;

    public Mono<User> get(String username) {
        return webClient.get()
                .uri("users/{username}", username)
                .retrieve()
                .bodyToMono(User.class);
    }
}
