package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.catalog.*;
import org.example.controller.restrequest.CreateEventRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.mapper.EventModelMapper;
import org.example.mapper.SoapEventMapper;
import org.example.model.model.EventDto;
import org.example.service.EventService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;


@Endpoint
@RequiredArgsConstructor
public class EventEndpoint {
    private static final String NAMESPACE_URI = "http://org/example/catalog";

    private final EventService eventService;
    private final EventModelMapper eventModelMapper;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEventRequest")
    @ResponsePayload
    public GetEventResponse getEvent(@RequestPayload GetEventRequest request) throws DatatypeConfigurationException {
        Long id = request.getId();
        System.out.println("id = " + id);
        GetEventResponse response = new GetEventResponse();
        System.out.println("1");
        EventDto eventdto = eventService.getEventById(id);

        System.out.println("2");
        response.setEvent(SoapEventMapper.fromEventDtoToSoap(eventdto));
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllEventsRequest")
    @ResponsePayload
    public GetAllEventsResponse getAllEvents(@RequestPayload GetAllEventsRequest request) throws Exception {
        GetAllEventsResponse r = new GetAllEventsResponse();
        var filter = request.getFilter();
        var sort =  request.getSort();
        var offset = request.getOffset();
        var limit = request.getLimit();

        var events = eventService.getAllEvents(filter, sort, limit, offset)
                .stream().map(eventDto -> {
                    try {
                        return SoapEventMapper.fromEventDtoToSoap(eventDto);
                    } catch (DatatypeConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        r.getEvents().addAll(events);
        return r;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addEventRequest")
    @ResponsePayload
    public AddEventResponse addEvent(@RequestPayload  AddEventRequest request) throws Exception {
        var r = new CreateEventRestRequest();
        if (request.getEvent() != null){
            var eventDto = SoapEventMapper.fromSoapToEventDto(request.getEvent());

            r.setEventType(eventDto.getEventType());
            r.setName(eventDto.getName());
            r.setMinAge(eventDto.getMinAge());
            r.setDate(eventDto.getDate());

            EventDto newEvent = eventService.createEvent(r);
            AddEventResponse resp = new AddEventResponse();
            resp.setEvent(SoapEventMapper.fromEventDtoToSoap(newEvent));
            return resp;
        } else {
            throw ErrorDescriptions.EVENT_REQUIRED.exception();
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateEventRequest")
    @ResponsePayload
    public UpdateEventResponse updateEvent(@RequestPayload UpdateEventRequest request) throws Exception {
        long id = request.getEventId();
        if (id == 0) {
            throw ErrorDescriptions.EVENT_ID_REQUIRED.exception();
        }
        var eventDto = SoapEventMapper.fromSoapToEventDto(request.getEvent());

        EventDto res = eventService.updateEventById(id, eventDto);
        UpdateEventResponse r = new UpdateEventResponse();
        r.setEvent(SoapEventMapper.fromEventDtoToSoap(res));
        return r;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteEventRequest")
    @ResponsePayload
    public DeleteEventResponse deleteEvent(@RequestPayload DeleteEventRequest request) throws Exception {
        var id = request.getEventId();
        if (id == 0) {
            throw ErrorDescriptions.EVENT_ID_REQUIRED.exception();
        }
        eventService.deleteEventById(id);
        var r = new DeleteEventResponse();
        r.setResponse("deleted");
        return r;
    }

}
