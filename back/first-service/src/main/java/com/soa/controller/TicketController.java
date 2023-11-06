package com.soa.controller;

import com.soa.common.Endpoints;
import com.soa.error.ErrorDescriptions;
import com.soa.model.CreateTicketRequest;
import com.soa.model.TicketDto;
import com.soa.model.enums.TicketType;
import com.soa.repository.FilterCriteria;
import com.soa.repository.SortCriteria;
import com.soa.service.TicketService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping(value = Endpoints.CREATE_TICKET)
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody CreateTicketRequest request
    ) {
        TicketDto ticketDto = ticketService.createTicket(request);
        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    /**
     * Получение списка билетов.
     *
     * @return список билетов.
     */
    @GetMapping(value = Endpoints.GET_ALL_TICKETS)
    public ResponseEntity<List<TicketDto>> getAllTickets(
            @RequestParam(value = "filter", required = false) String[] filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset
    ) throws Exception {
        if (limit != null) {
            if (limit <= 0) {
                throw ErrorDescriptions.INCORRECT_LIMIT.exception();
            }
        }

        if (offset != null) {
            if (offset < 0) {
                throw ErrorDescriptions.INCORRECT_OFFSET.exception();
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        var allowedFilters = List.of(
                "id",
                "name",
                "coordinateX",
                "coordinateY",
                "creationDate",
                "price",
                "discount",
                "refundable",
                "type"
        );

        List<FilterCriteria> filters = new ArrayList<>();
        if (filter != null){
            try {
                for (String f : filter) {
                    var key = f.split("\\[", 2)[0];
                    if (!allowedFilters.contains(key)) {
                        throw new Exception("Недопустимое значение фильтра " + key + ", должно быть одно иззначений: " + allowedFilters);
                    }

                    var val = f.split("\\]", 2)[1];
                    val = val.substring(1);
                    if (key.equals("refundable") && !(val.equals("true") | val.toLowerCase().equals("false"))){
                        throw new Exception("Недопустимое значение refundable: должно быть одно из значений: [true, false]");
                    } else if (key.equals("type")){
                        val = val.toUpperCase();
                        try {
                            TicketType.valueOf(val);
                        } catch (Exception exc) {
                            throw new Exception("Недопустимое значение type: должно быть одно из значений: [CHEAP, BUDGETARY, USUAL, VIP]");
                        }
                    } else if (key.equals("creationDate")) {
                        Date date = formatter.parse(val);
                        System.out.println(date);
                        if (date == null){
                            throw new Exception("Недопустимое значение creationDate: ожидается строка вида yyyy-MM-dd");
                        }
                    }

                    var op = f.split("\\[", 2)[1].split("\\]", 2)[0];

                    filters.add(
                            new FilterCriteria(
                                    key,
                                    op,
                                    key.equals("refundable") ? (Boolean) val.equals("true") : key.equals("type") ? TicketType.valueOf(val) : key.equals("creationDate") ? formatter.parse(val) : val
                            )
                    );
                }
            } catch (IndexOutOfBoundsException exc){
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }

        List<SortCriteria> sc = new ArrayList<>();
        if (sort != null) {
            try {
                var listSorts = Arrays.asList(sort.split(","));
                for (String oneSort : listSorts) {
                    var descSort = oneSort.charAt(0) == '-';
                    var key = "";
                    if (descSort) {
                        key = oneSort.substring(1);
                    } else {
                        key = oneSort;
                    }
                    sc.add(new SortCriteria(key, !descSort));
                }
            } catch (Exception e) {
                throw ErrorDescriptions.INCORRECT_SORT.exception();
            }
        }
        List<TicketDto> tickets = ticketService.getAllTickets(filters, sc, limit, offset);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    /**
     * Получение билет по id.
     *
     * @return список кораблей.
     */
    @GetMapping(value = Endpoints.GET_TICKET_BY_ID)
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        return new ResponseEntity<>(ticketService.getTicketById(ticketId), HttpStatus.OK);
    }

    /**
     * Удаление билета по id.
     */
    @DeleteMapping(value = Endpoints.DELETE_TICKET_BY_ID)
    public ResponseEntity<Object> deleteTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    /**
     * Обновление билета по id.
     */
    @PutMapping(value = Endpoints.UPDATE_TICKET_BY_ID)
    public ResponseEntity<Object> updateTicketById(
            @PathVariable("ticketId") Long ticketId,
            @RequestBody CreateTicketRequest request
    ) {
        var res = ticketService.updateTicketById(ticketId, request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

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

    @PostMapping(value = Endpoints.NEW_VIP_TICKET_BY_ID)
    public ResponseEntity<Object> createVipTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        var res = ticketService.newVipTicketById(ticketId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = Endpoints.DISCOUNT_TICKET_BY_ID)
    public ResponseEntity<Object> createDiscountTicketById(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("discount") Double discount
    ) {
        var res = ticketService.newDiscountTicketById(ticketId, discount);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
