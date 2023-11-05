package ru.egormit.starshipservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.egormit.starshipservice.error.decoder.CustomErrorDecoder;

/**
 * Конфигурация Feign.
 *
 * @author Egor Mitrofanov.
 */
@Configuration
@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@Import({FeignClientsConfiguration.class})
public class FeignConfiguration {

    /**
     * Создание энкодера
     */
    @Bean
    @Primary
    public Encoder customFeignEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }

    /**
     * Создание декодера
     */
    @Bean
    @Primary
    public Decoder customFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringDecoder(messageConverters);
    }

    /**
     * Создание декодера ошибок
     */
    @Bean
    public ErrorDecoder customErrorDecoder(ObjectMapper objectMapper) {
        return new CustomErrorDecoder(objectMapper);
    }

}
