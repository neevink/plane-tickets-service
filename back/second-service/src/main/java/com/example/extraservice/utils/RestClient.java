package com.example.extraservice.utils;

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

    public ResponseEntity<Object> createVipTicket(Integer id, Integer stepsCount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = mainServiceUrl + "/tickets/vip/" + id;
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
    }

    public ResponseEntity<Object> createVip(Integer ticketId, Integer stepsCount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = mainServiceUrl + "/labworks/" + ticketId + "/difficulty/increase/" + stepsCount;
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
    }

    public ResponseEntity<Object> makeHardcore(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = mainServiceUrl + "/labworks/discipline/" + id + "/make-hardcore";
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

}
