package com.cp.projects.messagingsystem.messagingserver.config;

import com.cp.projects.messagingsystem.messagingserver.config.properties.SecurityProperties;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecretKey secretKey(SecurityProperties securityProperties) {
        byte[] decoded = Decoders.BASE64.decode(securityProperties.getJwtSecret());
        return new SecretKeySpec(decoded, SignatureAlgorithm.HS512.getJcaName());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, SecurityProperties securityProperties) {
        return http
            .cors().configurationSource(corsConfigurationSource(securityProperties)).and()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties securityProperties) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(securityProperties.getAllowedOrigins());
        config.setAllowedMethods(securityProperties.getAllowedMethods());
        config.setExposedHeaders(securityProperties.getExposedHeaders());
        config.setAllowedHeaders(securityProperties.getAllowedHeaders());
        config.setAllowCredentials(securityProperties.isAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.setCorsConfigurations(Map.of("/**", config));
        return source;
    }
}
