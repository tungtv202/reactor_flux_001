package exercise.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
class ReactorApplicationTests {


//    @Test
    void apiTest() {
        var username = "Jasen64";
        WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build()
                .get()
                .uri("/api/recent_purchases/" + username)
                .exchange()
                .expectStatus().isOk();
    }

}
