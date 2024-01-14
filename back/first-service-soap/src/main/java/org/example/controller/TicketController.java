package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Endpoints;
import org.example.controller.restrequest.CreateTicketRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.model.enums.TicketType;
import org.example.model.model.TicketDto;
import org.example.repository.FilterCriteria;
import org.example.repository.SortCriteria;
import org.example.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping(value = Endpoints.CREATE_TICKET)
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody CreateTicketRestRequest request
    ) {
        TicketDto ticketDto = ticketService.createTicket(request);
        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    @GetMapping(value = Endpoints.GET_ALL_TICKETS)
    public ResponseEntity<List<TicketDto>> getAllTickets(
            @RequestParam(value = "filter", required = false) String[] filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset
    ) throws Exception {
        List<TicketDto> tickets = ticketService.getAllTickets(Arrays.stream(filter).toList(), sort, limit, offset);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping(value = Endpoints.GET_TICKET_BY_ID)
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        return new ResponseEntity<>(ticketService.getTicketById(ticketId), HttpStatus.OK);
    }

    @DeleteMapping(value = Endpoints.DELETE_TICKET_BY_ID)
    public ResponseEntity<Object> deleteTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

//    @PutMapping(value = Endpoints.UPDATE_TICKET_BY_ID)
//    public ResponseEntity<Object> updateTicketById(
//            @PathVariable("ticketId") Long ticketId,
//            @RequestBody CreateTicketRequest request
//    ) {
//        var res = ticketService.updateTicketById(ticketId, request);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

    // front needs this!!!!!!!!
    @GetMapping(value = Endpoints.GET_TICKETS_COUNT)
    public ResponseEntity<Long> countTickets() {
        return new ResponseEntity<>(ticketService.countTickets(), HttpStatus.OK);
    }


    @GetMapping(value = Endpoints.GET_TICKETS_TYPES)
    public ResponseEntity<List<Object>> eventsTypes() {
        return new ResponseEntity<>(ticketService.getTypes(), HttpStatus.OK);
    }


//- [] GET /tickets/discount/sum
    @GetMapping(value = Endpoints.GET_TICKETS_DISCOUNT_SUM)
    public ResponseEntity<Double> getSumOfDiscount() {
        return new ResponseEntity<>(ticketService.sumOfDiscount(), HttpStatus.OK);
    }

//-  GET /tickets/discount/count
    @GetMapping(value = Endpoints.GET_TICKETS_DISCOUNT_COUNT)
    public ResponseEntity<Object> getSumOfDiscountCount() {
        return new ResponseEntity<>(ticketService.sumOfDiscountCount(), HttpStatus.OK);
    }

//- [] GET /tickets/type/count

    @GetMapping(value = Endpoints.GET_TICKETS_TYPE_COUNT)
    public ResponseEntity<Object> getTicketsTypeCount(
            @RequestParam("type") TicketType type
    ) {
        if (type == null) {
            return new ResponseEntity<>("type parameter is required", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ticketService.getTicketsTypeCount(type), HttpStatus.OK);
    }

//    @PostMapping(value = Endpoints.NEW_VIP_TICKET_BY_ID)
//    public ResponseEntity<Object> createVipTicketById(
//            @PathVariable("ticketId") Long ticketId
//    ) {
//        var res = ticketService.newVipTicketById(ticketId);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

//    @PostMapping(value = Endpoints.DISCOUNT_TICKET_BY_ID)
//    public ResponseEntity<Object> createDiscountTicketById(
//            @PathVariable("ticketId") Long ticketId,
//            @PathVariable("discount") Double discount
//    ) {
//        var res = ticketService.newDiscountTicketById(ticketId, discount);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }
}
