package se.ifmo.ru.firstservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FirstServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FirstServiceApplication.class, args);

    }
}
