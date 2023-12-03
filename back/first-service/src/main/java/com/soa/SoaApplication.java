package com.soa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SoaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoaApplication.class, args);
    }

}
