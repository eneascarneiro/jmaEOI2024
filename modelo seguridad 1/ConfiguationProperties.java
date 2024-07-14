package com.example.jpa_formacion.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "general")
@Getter
@Setter
public class ConfiguationProperties {

    /**
     * IP of foo service used to blah.
     */
    private String ippythonserver = "192.168.100.128" ;

    // getter & setter
}