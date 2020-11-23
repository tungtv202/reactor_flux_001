package exercise.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableScheduling
public class ReactorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactorApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(PurchaseHandler handler) {
        return route(GET("/api/recent_purchases/{username}"), handler::purchasesRecent);
    }
}
