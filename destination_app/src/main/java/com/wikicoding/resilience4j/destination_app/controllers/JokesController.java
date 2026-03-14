package com.wikicoding.resilience4j.destination_app.controllers;

import com.wikicoding.resilience4j.destination_app.domain.Joke;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/jokes")
public class JokesController {
    @GetMapping
    public ResponseEntity<Object> getJoke() {
        return ResponseEntity.ok().body(new Joke(UUID.randomUUID().toString(), "this is a joke"));
    }
}
