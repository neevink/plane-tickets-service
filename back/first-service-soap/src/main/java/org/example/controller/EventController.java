package org.example.controller;

import org.example.catalog.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class EventController {
    private static final String NAMESPACE_URI = "http://org/example/catalog";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEventRequest")
    @ResponsePayload
    public GetEventResponse getEvent(@RequestPayload GetEventRequest request){
        Long id = request.getId();
        System.out.println("id = " + id);
        GetEventResponse response = new GetEventResponse();
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setName("hello!");
        response.setEvent(eventDto);
        return response;
    }
}
