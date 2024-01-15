package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.catalog.*;
import org.example.common.Endpoints;
import org.example.controller.restrequest.CreateTicketRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.mapper.SoapEventMapper;
import org.example.mapper.SoapTicketMapper;
import org.example.mapper.TicketModelMapper;
import org.example.model.enums.TicketType;
import org.example.model.model.EventDto;
import org.example.model.model.TicketDto;
import org.example.repository.TicketRepository;
import org.example.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.restrequest.CreateEventRestRequest;
import org.example.controller.restrequest.CreateTicketRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.mapper.EventModelMapper;
import org.example.mapper.TicketModelMapper;
import org.example.model.enums.TicketType;
import org.example.model.model.*;
import org.example.repository.*;
import org.hibernate.tuple.CreationTimestampGeneration;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import javax.xml.datatype.DatatypeConfigurationException;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Endpoint
@AllArgsConstructor
public class TicketEndpoint {

    // get by id

    private static final String NAMESPACE_URI = "http://org/example/catalog";

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final TicketModelMapper ticketModelMapper;


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getTicketRequest")
    @ResponsePayload
    public GetTicketResponse getTicket(@RequestPayload GetTicketRequest request) throws DatatypeConfigurationException {
        var id =request.getId();

        TicketDto ticketDto = ticketService.getTicketById(id);

        var r = new GetTicketResponse();
        r.setTicket(SoapTicketMapper.fromTicketDtoToSoap(ticketDto));

        return r;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllTicketsRequest")
    @ResponsePayload
    public GetAllTicketsResponse getAllTickets(@RequestPayload GetAllTicketsRequest request) throws Exception {
        var filter = request.getFilter();
        var sort =  request.getSort();
        var offset = request.getOffset();
        var limit = request.getLimit();

        GetAllTicketsResponse r = new GetAllTicketsResponse();

        var events = ticketService.getAllTickets(filter, sort, limit, offset)
                .stream().map(ticketDto -> {
                    try {
                        return SoapTicketMapper.fromTicketDtoToSoap(ticketDto);
                    } catch (DatatypeConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        r.getTickets().addAll(events);
        return r;

    }



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addTicketRequest")
    @ResponsePayload
    public AddTicketResponse addTicket(@RequestPayload AddTicketRequest addTicketRequest) throws Exception {
        var r = new CreateTicketRestRequest();
        var request = addTicketRequest.getTicket();
        if (request != null){
            var ticketDto = SoapTicketMapper.fromSoapToTicketDto(request);
            r.setName(ticketDto.getName());
            r.setCoordinates(ticketDto.getCoordinates());
            r.setPrice(ticketDto.getPrice());
            r.setDiscount(ticketDto.getDiscount());
            r.setRefundable(ticketDto.getRefundable());
            r.setType(ticketDto.getType());
            r.setEvent(ticketDto.getEvent());

            TicketDto newTicket = ticketService.createTicket(r);
            AddTicketResponse resp = new AddTicketResponse();
            resp.setTicket(SoapTicketMapper.fromTicketDtoToSoap(newTicket));

            return resp;
        } else {
            throw ErrorDescriptions.EVENT_REQUIRED.exception();
        }
    }


    // delete by id
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteTicketRequest")
    @ResponsePayload
    public DeleteTicketResponse deleteTicket(@RequestPayload DeleteTicketRequest request) throws Exception {
        var id = request.getTicketId();
        if (id == 0) {
            throw ErrorDescriptions.TICKET_ID_REQUIRED.exception();
        }
        ticketService.deleteTicketById(id);
        var r = new DeleteTicketResponse();
        r.setResponse("deleted");
        return r;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateTicketRequest")
    @ResponsePayload
    public UpdateTicketResponse updateTicket(@RequestPayload UpdateTicketRequest request) throws Exception {
        var id = request.getTicketId();
        if (id == 0) {
            throw ErrorDescriptions.TICKET_ID_REQUIRED.exception();
        }
        var ticketDto = SoapTicketMapper.fromSoapToTicketDto(request.getTicket());
        TicketDto res = ticketService.updateTicketById(id, ticketDto);
        var r = new UpdateTicketResponse();
        r.setTicket(SoapTicketMapper.fromTicketDtoToSoap(res));
        return r;
    }

    // sum of discount
    // group by discount
    // count of tickets type < sometype


    //- [] GET /tickets/discount/sum
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSumOfDiscountRequest")
    @ResponsePayload
    public GetSumOfDiscountResponse getSumOfDiscount() {
        var sum = ticketService.sumOfDiscount();
        var hui = new GetSumOfDiscountResponse();
        hui.setSum(sum);
        return hui;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDiscountCountRequest")
    @ResponsePayload
    public GetDiscountCountResponse getDiscountCount(@RequestPayload GetDiscountCountRequest getDiscountCountRequest) {
        var all = ticketRepository.findAll();
//        [
//        {discount: 10
//         count: 100}
//        {discount: 11
//         count: 101}
//        ]
        var groupedByDiscount = StreamSupport.stream(all.spliterator(), false)
                .collect(groupingBy(
                        Ticket::getDiscount
                ));
        var res = new ArrayList<Count>();

        for(var t : groupedByDiscount.entrySet()){
            var c = new Count();
            c.setDiscount(t.getKey());
            c.setCount(t.getValue().size());
            res.add(c);
        }
        GetDiscountCountResponse r = new GetDiscountCountResponse();
        r.getCount().addAll(res);
        return r;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getTicketsTypeCountRequest")
    @ResponsePayload
    public GetTicketsTypeCountResponse getTicketsTypeCount(@RequestPayload GetTicketsTypeCountRequest getTicketsTypeCountRequest) {
        String type = getTicketsTypeCountRequest.getType();
        if (type == null) {
            throw ErrorDescriptions.TYPE_REQUIRED.exception();
        }

        var typeEnum = TicketType.valueOf(type);
        long resp = ticketService.getTicketsTypeCount(typeEnum);

        var count = new GetTicketsTypeCountResponse();
        count.setCount((int) resp);
        return count;
    }

}
