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

    @PostMapping(value = "/vip/{ticket-id}/{person-id}")
    public ResponseEntity<?> increaseStepsCount(@PathVariable("ticket-id") Integer ticketId, @PathVariable("person-id") Integer personId) {
        return sellingService.increaseStepsCount(ticketId, personId);
    }

    @PostMapping(value = "/discount/{ticket-id}/{person-id}/{discount}")
    public ResponseEntity<?> makeHardcore(
            @PathVariable("ticket-id") Integer ticketId,
            @PathVariable("person-id") Integer personId,
            @PathVariable("discount") Double discount
    ) {
        return sellingService.makeDiscount(ticketId, personId, discount);
    }
}
