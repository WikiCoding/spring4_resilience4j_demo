package com.wikicoding.resilience4j.source_app.services;

import com.wikicoding.resilience4j.source_app.domain.Joke;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokesService {
    private final RestTemplate restTemplate;
    @Value("${jokes.api.url}")
    private String apiUrl;

//    springboot 4 integrated retries
    @Retryable(includes = { ResourceAccessException.class, ConnectException.class },
            maxRetries = 4,
            delay = 1000,
            multiplier = 2 /*exponential backoff*/)

    @CircuitBreaker(name = "jokesService")
    @Retry(name = "jokesService", fallbackMethod = "fallback")
    public Joke getJoke() {
        log.debug("fetch joke from {}", apiUrl);
        return restTemplate.getForObject(apiUrl, Joke.class);
    }

    public Joke fallback(Throwable throwable) {
        throw new RuntimeException("3 retries done but got: " + throwable.getMessage());
    }
}
