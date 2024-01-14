package com.soa.controller;

import com.soa.error.ErrorDescriptions;
import com.soa.exception.ServiceFault;
import com.soa.exception.ServiceFaultException;
import com.soa.model.EventDto;
import com.soa.model.EventDto2;
import com.soa.model.GetEventResponse;
import com.soa.model.enums.EventType;
import com.soa.model.request.GetEventRequest;
import com.soa.model.response.EventsResponse;
import com.soa.repository.FilterCriteria;
import com.soa.repository.SortCriteria;
import com.soa.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Endpoint
@Slf4j
public class EventEndpoint {
    private static final String NAMESPACE_URI = "http://se/ifmo/ru/firstservice/catalog";
//    private final FlatMapper flatMapper;
//    private final ResponseUtils responseUtils;

    private final EventService eventService;

    @Autowired
    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEventRequest")
    @ResponsePayload
    public EventDto2 getEventRequest(@RequestPayload GetEventRequest getEventRequest) {
        System.out.println("hello im here");
        return EventDto2.of(1L, "hello");
//        return GetEventResponse.of(EventDto2.of(1L, "hello"));
//        return eventService.getEventById(getEventRequest.getId());
    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllEventsRequest")
//    @ResponsePayload
//    public EventsResponse getAllEventsRequest(@RequestPayload GetAllEventsRequest request) throws Exception{
//
//        Long limit = request.getLimit();
//        Long offset = request.getOffset();
//
//        if (limit != null) {
//            if (limit <= 0) {
//                throw ErrorDescriptions.INCORRECT_LIMIT.exception();
//            }
//        }
//
//        if (offset != null) {
//            if (offset < 0) {
//                throw ErrorDescriptions.INCORRECT_OFFSET.exception();
//            }
//        }
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
//        var allowedFilters = List.of(
//                "id",
//                "name",
//                "date",
//                "minAge",
//                "eventType"
//        );
//
//        List<FilterCriteria> filters = new ArrayList<>();
//
//        String[] filter = request.getFilter();
//        if (filter != null){
//            try {
//                for (String f : filter) {
//                    var key = f.split("\\[", 2)[0];
//                    if (!allowedFilters.contains(key)) {
//                        throw new Exception("Недопустимое значение фильтра " + key + ", должно быть одно иззначений: " + allowedFilters);
//                    }
//
//                    var val = f.split("\\]", 2)[1];
//                    val = val.substring(1);
//                    if (key.equals("eventType")){
//                        val = val.toUpperCase();
//                        try {
//                            EventType.valueOf(val);
//                        } catch (Exception exc) {
//                            throw new Exception("Недопустимое значение eventType: должно быть одно из значений: [CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE]");
//                        }
//                    } else if (key.equals("date")) {
//                        Date date = formatter.parse(val);
//                        System.out.println(date);
//                        if (date == null){
//                            throw new Exception("Недопустимое значение date: ожидается строка вида yyyy-MM-dd");
//                        }
//                    }
//
//                    var op = f.split("\\[", 2)[1].split("\\]", 2)[0];
//
//                    filters.add(
//                            new FilterCriteria(
//                                    key,
//                                    op,
//                                    key.equals("eventType") ? EventType.valueOf(val) : key.equals("date") ? formatter.parse(val) : val
//                            )
//                    );
//                }
//            } catch (IndexOutOfBoundsException exc){
//                throw ErrorDescriptions.INCORRECT_FILTER.exception();
//            }
//        }
//
//        List<SortCriteria> sc = new ArrayList<>();
//        String sort = request.getSort();
//        if (sort != null) {
//            try {
//                var listSorts = Arrays.asList(sort.split(","));
//                for (String oneSort : listSorts) {
//                    var descSort = oneSort.charAt(0) == '-';
//                    var key = "";
//                    if (descSort) {
//                        key = oneSort.substring(1);
//                    } else {
//                        key = oneSort;
//                    }
//                    sc.add(new SortCriteria(key, !descSort));
//                }
//            } catch (Exception e) {
//                throw ErrorDescriptions.INCORRECT_SORT.exception();
//            }
//        }
//
//        List<EventDto> events = eventService.getAllEvents(filters, sc, limit, offset);
//        return new EventsResponse(events);
//    }



//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFlatRequest")
//    @ResponsePayload
//    public GetFlatResponse getFlat(@RequestPayload GetFlatRequest request){
//        String id = request.getId();
//
//        log.info("id = {}", id);
//
//        Flat flat = null;
//        try {
//            flat = flatService.getFlat(Long.parseLong(id));
//        } catch (NumberFormatException e) {
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
//        }
//
//        if (flat == null){
//            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + id + " not found"));
//        }
//
//        GetFlatResponse response = new GetFlatResponse();
//
//        response.setFlat(flatMapper.toGetResponseDtoResponse(flatMapper.toDto(flat)));
//
//        return response;
//    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addFlatRequest")
//    @ResponsePayload
//    public AddFlatResponse addFlat(@RequestPayload AddFlatRequest request) {
//
//        FlatAddOrUpdateRequestDto flatNew = flatMapper.toFlatAddOrUpdateRequestDto(request);
//
//        log.info(flatNew.toString());
//
//        validateFlatAddOrUpdateRequestDto(flatNew);
//
//        log.info("Validate - TRUE");
//        log.info(flatNew.toString());
//
//        AddFlatResponse response = new AddFlatResponse();
//        response.setFlat(flatMapper.toGetResponseDto(flatService.addFlat(flatNew)));
//
//        return response;
//    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateFlatRequest")
//    @ResponsePayload
//    public UpdateFlatResponse updateFlate(@RequestPayload UpdateFlatRequest request){
//
//        FlatAddOrUpdateRequestDto flatNew = flatMapper.toFlatAddOrUpdateRequestDto(request.getFlat());
//        String id = request.getId();
//
//
//        log.info("UPDATE START");
//        log.info("Id = {}", request.getId());
//        log.info("Flat = {}", flatNew.toString());
//
//        validateFlatAddOrUpdateRequestDto(flatNew);
//
//        Flat flat = null;
//
//        try {
//            flat = flatService.updateFlat(Long.parseLong(id), flatNew);
//        } catch (NumberFormatException e) {
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
//        }
//
//        log.info("UPDATE - OK");
//
//        if (flat == null){
//            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + request.getId() + " not found"));
//        }
//
//        UpdateFlatResponse response = new UpdateFlatResponse();
//
//        response.setFlat(flatMapper.toGetResponseDto(flat));
//
//        return response;
//    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteFlatRequest")
//    @ResponsePayload
//    public DeleteFlatResponse deleteFlat(@RequestPayload DeleteFlatRequest request){
//
//        boolean deleted = false;
//
//        try {
//            deleted =  flatService.deleteFlat(Long.parseLong(request.getId()));
//        } catch (NumberFormatException e) {
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
//        }
//
//        log.info("DeleteById - {}", deleted);
//
//        if (!deleted){
//            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + request.getId() + " not found"));
//        }
//
//        DeleteFlatResponse response = new DeleteFlatResponse();
//
//        response.setCode("204");
//        response.setMessage("The flat was successfully deleted");
//        response.setTime(String.valueOf(LocalDateTime.now()));
//
//        return response;
//    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteFlatByViewRequest")
//    @ResponsePayload
//    public DeleteFlatByViewResponse deleteFlatByView(@RequestPayload DeleteFlatByViewRequest request){
//        boolean deleted = false;
//
//        log.info(request.getView());
//        log.info(View.fromValue(request.getView().toLowerCase()).toString());
//
//        try {
//            deleted = flatService.deleteOneFlatByView(View.fromValue(request.getView().toLowerCase()));
//        } catch (NotFoundException e){
//            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with view " + request.getView() + " not found"));
//        }
//
//        log.info("DeleteByView - {}, view is {}", deleted, request.getView().toLowerCase());
//
//        if (!deleted){
//            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with view " + request.getView() + " not found"));
//        }
//
//        DeleteFlatByViewResponse response = new DeleteFlatByViewResponse();
//
//        response.setCode("204");
//        response.setMessage("The flat was successfully deleted");
//        response.setTime(String.valueOf(LocalDateTime.now()));
//
//        return response;
//    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFlatsAverageTimeMetroRequest")
//    @ResponsePayload
//    public GetFlatsAverageTimeMetroResponse getFlatsAverageTimeMetro(){
//
//        GetFlatsAverageTimeMetroResponse response = new GetFlatsAverageTimeMetroResponse();
//
//        log.info("Average time to metro: {}", flatService.getFlatsAverageTimeToMetroOnFoot());
//
//        response.setNumber(flatService.getFlatsAverageTimeToMetroOnFoot());
//
//        return response;
//    }
//
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUniqueFlatViewRequest")
//    @ResponsePayload
//    public GetUniqueFlatViewResponse getUniqueFlatsView(){
//
//        GetUniqueFlatViewResponse response = new GetUniqueFlatViewResponse();
//
//        log.info("List unique view: {}", flatService.getUniqueFlatsView());
//
//        response.getViewList().addAll(flatService.getUniqueFlatsView());
//
//        return response;
//    }
//
//    private void validateFlatAddOrUpdateRequestDto(FlatAddOrUpdateRequestDto requestDto){
//        if (StringUtils.isEmpty(requestDto.getName()) || requestDto.getName().length() > 255){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Name не должен быть пустым и не больше 255 символов!"));
//        }
//
//        if (requestDto.getCoordinates() == null){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates не может быть null!"));
//        }
//
//        if (requestDto.getCoordinates().getX() == null){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates X не может быть null!"));
//        }
//
//        if (requestDto.getCoordinates().getY() == null){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates Y не может быть null!"));
//        }
//
//        if (requestDto.getArea() <= 0 ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Area должен быть больше 0!"));
//        }
//
//        if (requestDto.getNumberOfRooms() <= 0 ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Number Of Rooms должен быть больше 0!"));
//        }
//
//        if (requestDto.getFloor() <= 0 ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Floor должен быть больше 0!"));
//        }
//
//        if (requestDto.getTimeToMetroOnFoot() <= 0 ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Time To Metro On Foot должен быть больше 0!"));
//        }
//
//        if (requestDto.getBalcony() == null ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Balcony не может быть null!"));
//        }
//
//        if (requestDto.getPrice() <= 0 ){
//            throw new ServiceFaultException("Error", new ServiceFault("400", "Price должен быть больше 0!"));
//        }
//
//        if (requestDto.getHouse() != null){
//            if (requestDto.getHouse().getYear() != null) {
//                try {
//                    if (requestDto.getHouse().getYear() <= 0) {
//                        throw new ServiceFaultException("Error", new ServiceFault("400", "House Year должен быть больше 0!"));
//                    }
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (requestDto.getHouse().getNumberOfFloors() != null) {
//                try {
//                    if (requestDto.getHouse().getNumberOfFloors() <= 0) {
//                        throw new ServiceFaultException("Error", new ServiceFault("400", "House Number Of Floors должен быть больше 0!"));
//                    }
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
