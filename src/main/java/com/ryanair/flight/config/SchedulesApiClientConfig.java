package com.ryanair.flight.config;

import com.ryanair.flight.client.SchedulesApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SchedulesApiClientConfig {
    @Bean
    public SchedulesApiClient createSchedulesApiClient() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://services-api.ryanair.com/timtbl/3/schedules";
        return new SchedulesApiClient(restTemplate, baseUrl);
    }
}
