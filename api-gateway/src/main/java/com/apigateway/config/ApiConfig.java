package com.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor beanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("lbRestClientPostProcessor");
    }
}
