package com.cp.projects.messagingsystem.components.webfluxmongostarter.autoconfig;

import com.cp.projects.messagingsystem.components.webfluxmongostarter.properties.MongoProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ MongoProperties.class })
@PropertySource(value = "classpath:/webflux-mongo-starter-properties.yml", factory = YamlPropertySourceFactory.class)
public class MongoAutoConfig extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabaseName();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        super.configureClientSettings(builder);
        builder.applyConnectionString(
            new ConnectionString(mongoProperties.getConnectionString())
        );
    }

    @Override
    protected void configureConverters(MongoConverterConfigurationAdapter adapter) {
        super.configureConverters(adapter);

        Converter<OffsetDateTime, String> odtToString = new Converter<>() {
            @Override
            public String convert(OffsetDateTime source) {
                return source.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of(ZoneOffset.UTC.getId())));
            }
        };

        Converter<String, OffsetDateTime> stringToOdt = new Converter<>() {
            @Override
            public OffsetDateTime convert(String source) {
                return OffsetDateTime.parse(source);
            }
        };

        adapter.registerConverters(List.of(odtToString, stringToOdt));
    }
}
