package com.ryanair.flight.client;

import com.ryanair.flight.domain.Schedule;
import com.ryanair.flight.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulesApiClient {
    private static final String URL_FORMAT = "%s/%s/%s/years/%s/month/%s";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public Schedule getSchedule(String departure, String arrival, String year, String month) {
        String url = String.format(URL_FORMAT, baseUrl, departure, arrival, year, month);
        log.info("Request URL: {}", url);
        ResponseEntity<Schedule> entity = restTemplate.exchange(url, HttpMethod.GET, null, Schedule.class);
        if (!entity.getStatusCode().equals(HttpStatus.OK)) {
            throw new ExternalApiException(url, entity.getStatusCodeValue());
        }
        return entity.getBody();
    }
}
