package com.wikicoding.resilience4j.destination_app.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Joke {
    private String jokeId;
    private String joke;
}
