package com.example.extraservice.services;

import com.example.extraservice.utils.RestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class SellingService {
    private final RestClient restClient;

    public SellingService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<?> increaseStepsCount(Integer ticketId, Integer personId) {
        try {
            return restClient.createVipTicket(ticketId, personId);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(422).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(504).body("Service unavailable");
        }
    }

    public ResponseEntity<?> makeHardcore(Integer id) {
        try {
            return restClient.makeHardcore(id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(422).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(504).body("Service unavailable");
        }
    }

}
