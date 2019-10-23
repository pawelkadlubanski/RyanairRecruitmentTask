package com.ryanair.flight.config;

import com.ryanair.flight.client.RoutsApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RoutsApiClientConfig {
    @Bean
    public RoutsApiClient createRoutsApiClient() {
        RestTemplate restTemplate = new RestTemplate();
        String routsApiUrl = "https://services-api.ryanair.com/locate/3/routes";
        return new RoutsApiClient(restTemplate, routsApiUrl);
    }
}
