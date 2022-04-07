package com.cp.projects.messagingsystem.aggregatesapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource) {
        return http
            .cors().configurationSource(corsConfigurationSource).and()
            .csrf().disable()

            .exceptionHandling(spec -> spec
                .authenticationEntryPoint((ex, e) ->
                    Mono.fromRunnable(() -> ex.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                )
                .accessDeniedHandler((ex, e) ->
                    Mono.fromRunnable(() -> ex.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                )
            )

            .securityMatcher(new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(
                "/actuator/health"
            )))

            .authorizeExchange(ex -> ex
                .anyExchange().permitAll()
            )

            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .build();
    }
}
