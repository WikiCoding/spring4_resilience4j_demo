package com.wikicoding.resilience4j.source_app.services;

import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryLoggingListener {
    private final RetryRegistry retryRegistry;

    @PostConstruct
    public void registryRetryLogging() {
        retryRegistry.retry("jokesService").getEventPublisher().onRetry(event ->
                log.warn("Retry attempt {} for {} due to {}",
                event.getNumberOfRetryAttempts(),
                event.getName(),
                event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : null));
    }
}
