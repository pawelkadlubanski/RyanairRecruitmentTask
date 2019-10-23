package com.ryanair.flight.client;

import com.ryanair.flight.domain.Rout;
import com.ryanair.flight.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoutsApiClient {
    private final RestTemplate restTemplate;
    private final String url;

    public List<Rout> getRouts() {
        ResponseEntity<List<Rout>> entity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Rout>>(){});
        if (!entity.getStatusCode().equals(HttpStatus.OK)) {
            throw new ExternalApiException(url, entity.getStatusCodeValue());
        }
        return entity.getBody();
    }
}
