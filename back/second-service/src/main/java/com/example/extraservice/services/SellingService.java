package com.example.extraservice.services;

import com.example.extraservice.model.ErrorMessage;
import com.example.extraservice.utils.RestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

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
            ErrorMessage err = new ErrorMessage();
            List l = new ArrayList();
            l.add(e.getMessage());
            err.setErrors(l);
            return ResponseEntity.status(504).body(err);
        }
    }

    public ResponseEntity<?> makeDiscount(Integer ticketId, Integer personId, Double discount) {
        try {
            return restClient.makeDiscountTicket(ticketId, personId, discount);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(422).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            ErrorMessage err = new ErrorMessage();
            List l = new ArrayList();
            l.add(e.getMessage());
            err.setErrors(l);
            return ResponseEntity.status(504).body(err);
        }
    }

}
