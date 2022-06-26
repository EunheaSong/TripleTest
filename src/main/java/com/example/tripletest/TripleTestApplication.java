package com.example.tripletest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TripleTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripleTestApplication.class, args);
    }

}
