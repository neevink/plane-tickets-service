package com.service.discovery.serverapp.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestClient {
    private final RestTemplate restTemplate;

    @Value("${main-service.url}")
    private String mainServiceUrl;

    public ResponseEntity<Object> createVipTicket(Integer ticketId, Integer personId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = mainServiceUrl + "/tickets/vip/" + ticketId;
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

    public ResponseEntity<Object> makeDiscountTicket(Integer ticketId, Integer personId, Double discount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = mainServiceUrl + "/tickets/discount/" + ticketId + "/" + discount;
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

}
