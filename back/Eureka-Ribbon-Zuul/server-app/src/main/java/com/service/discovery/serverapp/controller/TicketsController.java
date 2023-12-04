package com.service.discovery.serverapp.controller;

import com.service.discovery.serverapp.services.SellingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("booking/sell")
public class TicketsController {

    private final SellingService sellingService;

    public TicketsController(SellingService sellingService) {
        this.sellingService = sellingService;
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

    @GetMapping("test")
    public  String test(){
        return "port " + port;
    }

    @Value("${server.port}")
    private String port;

}
