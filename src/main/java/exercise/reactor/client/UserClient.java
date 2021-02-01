package exercise.reactor.client;

import exercise.reactor.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class UserClient {
    @Autowired
    private WebClient webClient;

    public Mono<User> get(String username) {
        return webClient.get()
                .uri("users/{username}", username)
                .retrieve()
                .bodyToMono(User.class)
                .doOnError(e -> log.error(e.getMessage()))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
