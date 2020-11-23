package exercise.reactor.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ClientConfig {
    @Value("${client.base_url}")
    private String baseUrl;

    @Bean
    @Primary
    public WebClient webClient() {
        var strategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new CustomizeJsonDecoder(objectMapper(), MediaType.APPLICATION_JSON));
                })
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
