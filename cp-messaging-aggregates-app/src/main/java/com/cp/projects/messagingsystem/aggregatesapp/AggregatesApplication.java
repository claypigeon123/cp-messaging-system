package com.cp.projects.messagingsystem.aggregatesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { ReactiveUserDetailsServiceAutoConfiguration.class })
public class AggregatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregatesApplication.class, args);
    }
}
