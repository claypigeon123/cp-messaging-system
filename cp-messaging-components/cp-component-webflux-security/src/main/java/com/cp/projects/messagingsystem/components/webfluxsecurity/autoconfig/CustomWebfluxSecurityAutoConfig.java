package com.cp.projects.messagingsystem.components.webfluxsecurity.autoconfig;

import com.cp.projects.messagingsystem.components.webfluxsecurity.properties.SecurityProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({ SecurityProperties.class })
@PropertySource(value = "classpath:webflux-security-properties.yml", factory = YamlPropertySourceFactory.class)
public class CustomWebfluxSecurityAutoConfig {

    @Bean
    public SecretKey secretKey(SecurityProperties securityProperties) {
        byte[] decoded = Decoders.BASE64.decode(securityProperties.getJwtSecret());
        return new SecretKeySpec(decoded, SignatureAlgorithm.HS512.getJcaName());
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
