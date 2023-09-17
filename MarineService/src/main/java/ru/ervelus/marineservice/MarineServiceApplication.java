package ru.ervelus.marineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarineServiceApplication.class, args);
    }

}
