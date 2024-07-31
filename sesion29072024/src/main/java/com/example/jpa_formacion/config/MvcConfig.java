package com.example.jpa_formacion.config;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@ComponentScan(basePackages = "com.example.jpa_formacion.config")
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    Environment env;

    public MvcConfig() {
        super();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.forLanguageTag("es_ES"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    /*
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // For examples using Spring 4.1.0
        System.out.println("resource.handler.conf");
        System.out.println(env.getProperty("resource.handler.conf"));

        if ((env.getProperty("resource.handler.conf")).equals("4.1.0")) {
            registry.addResourceHandler("/files/**").addResourceLocations("file:///D:/solovmwarewalgreen/projecto/files").
                    setCachePeriod(3600).resourceChain(true).
                    addResolver(new PathResourceResolver());
        }
        // For examples using Spring 4.0.7
        else if ((env.getProperty("resource.handler.conf")).equals("4.0.7")) {
            registry.addResourceHandler("/files/**").addResourceLocations("file:///D:/solovmwarewalgreen/projecto/files");

        }
    }
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}