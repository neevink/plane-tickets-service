package com.example.extraservice.controllers;

import com.example.extraservice.services.SellingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("booking/sell")
public class TicketsController {
    private final SellingService sellingService;

    public TicketsController(SellingService sellingService) {
        this.sellingService = sellingService;
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PutMapping(value = "/vip/{ticket-id}/{person-id}")
    public ResponseEntity<?> increaseStepsCount(@PathVariable("ticket-id") Integer ticketId, @PathVariable("person-id") Integer personId) {
        return sellingService.increaseStepsCount(ticketId, personId);
    }

    @PostMapping(value = "/disciplines/{discipline-id}/make-hardcore")
    public ResponseEntity<?> makeHardcore(@PathVariable("discipline-id") Integer id) {
        return sellingService.makeHardcore(id);
    }
}
