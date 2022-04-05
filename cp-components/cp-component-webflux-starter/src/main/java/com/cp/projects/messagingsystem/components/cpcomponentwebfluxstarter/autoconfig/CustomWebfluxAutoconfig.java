package com.cp.projects.messagingsystem.components.cpcomponentwebfluxstarter.autoconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class CustomWebfluxAutoconfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Module odtModule() {
        SimpleModule module = new SimpleModule();

        JsonSerializer<OffsetDateTime> serializer = new JsonSerializer<>() {
            @Override
            public void serialize(OffsetDateTime odt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                String formatted = odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of(ZoneOffset.UTC.getId())));
                jgen.writeString(formatted);
            }
        };
        JsonDeserializer<OffsetDateTime> deserializer = new JsonDeserializer<>() {
            @Override
            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return OffsetDateTime.parse(jsonParser.getValueAsString());
            }
        };

        module.addSerializer(OffsetDateTime.class, serializer);
        module.addDeserializer(OffsetDateTime.class, deserializer);

        return module;
    }

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        HttpClient httpClient = HttpClient.create();
        httpClient.warmup().block();

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .codecs(configurer -> {
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            })
            .build();
    }

    @Bean
    @Primary
    @ConditionalOnBean(name = "webClientReportingFilter", value = ExchangeFilterFunction.class)
    public WebClient webClient(ObjectMapper objectMapper, ExchangeFilterFunction webClientReportingFilter) {
        return webClient(objectMapper).mutate()
            .filters(filters -> filters.add(webClientReportingFilter))
            .build();
    }
}
