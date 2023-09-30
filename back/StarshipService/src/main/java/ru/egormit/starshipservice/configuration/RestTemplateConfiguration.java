package ru.egormit.starshipservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.egormit.starshipservice.error.handler.ErrorHandler;

@Configuration(value = "IntegrationRestTemplateConfiguration")
public class RestTemplateConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build();
    }

    @Bean
    @Qualifier("serviceErrorHandler")
    @ConditionalOnMissingBean(name = "serviceErrorHandler")
    public ErrorHandler serviceErrorHandler(ObjectMapper objectMapper) {
        return new ErrorHandler(objectMapper);
    }

}
