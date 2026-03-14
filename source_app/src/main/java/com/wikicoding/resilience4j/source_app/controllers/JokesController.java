package com.wikicoding.resilience4j.source_app.controllers;

import com.wikicoding.resilience4j.source_app.services.JokesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokesController {
    private final JokesService jokesService;

    @GetMapping
    public ResponseEntity<Object> getJokes() {
        try {
            var response = jokesService.getJoke();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("A downstream service is unavailable.");
        }
    }
}
