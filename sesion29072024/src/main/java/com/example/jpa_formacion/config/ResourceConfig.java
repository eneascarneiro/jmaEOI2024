package com.example.jpa_formacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class ResourceConfig implements WebMvcConfigurer
{
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS =
            {
                    "classpath:/resources/",
                    "classpath:/static/",
                    "classpath:/"
            };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .setCachePeriod(3000)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        ;
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("file:///D:/solovmwarewalgreen/projecto/SEN4CFARMING/api/files/")
                .setCachePeriod(3000)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        ;
    }
}