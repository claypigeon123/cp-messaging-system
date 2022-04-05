package com.cp.projects.messagingsystem.components.ymlfactory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) {
        Properties propertiesFromYaml = loadYamlIntoProperties(encodedResource);
        String sourceName = name == null ? encodedResource.getResource().getFilename() : name;
        return new PropertiesPropertySource(Objects.requireNonNull(sourceName), propertiesFromYaml);
    }

    private Properties loadYamlIntoProperties(EncodedResource encodedResource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
