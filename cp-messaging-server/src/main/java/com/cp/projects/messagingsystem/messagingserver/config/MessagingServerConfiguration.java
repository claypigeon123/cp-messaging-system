package com.cp.projects.messagingsystem.messagingserver.config;

import com.cp.projects.messagingsystem.messagingserver.config.properties.MongoProperties;
import com.cp.projects.messagingsystem.messagingserver.config.properties.SecurityProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({ SecurityProperties.class, MongoProperties.class})
public class MessagingServerConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Module odtModule() {
        SimpleModule module = new SimpleModule();
        JsonSerializer<OffsetDateTime> serializer = new JsonSerializer<>() {
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String formatted = offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of(ZoneOffset.UTC.getId())));
                jsonGenerator.writeString(formatted);
            }
        };
        JsonDeserializer<OffsetDateTime> deserializer = new JsonDeserializer<>() {
            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String iso = jsonParser.getValueAsString();
                return OffsetDateTime.parse(iso);
            }
        };

        module.addSerializer(OffsetDateTime.class, serializer);
        module.addDeserializer(OffsetDateTime.class, deserializer);
        return module;
    }
}
