package com.wikicoding.resilience4j.source_app.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.retry.RetryListener;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JokesRetryListener implements RetryListener {
    @Override
    public void beforeRetry(RetryPolicy retryPolicy, Retryable<?> retryable) {
        RetryListener.super.beforeRetry(retryPolicy, retryable);
        log.info("retrying...");
    }
}
