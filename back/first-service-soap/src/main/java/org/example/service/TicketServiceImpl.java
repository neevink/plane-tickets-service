package org.example.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final EventService eventService;

    private final TicketModelMapper ticketModelMapper;

    private final EventModelMapper eventModelMapper;

    @Override
    @Transactional // added because of error
    public TicketDto createTicket(CreateTicketRestRequest request) {
        if (request.getRefundable() == null) {
            throw ErrorDescriptions.REFUNDABLE_MUST_PRESENT.exception();
        }


        if (request.getCoordinates() == null ||
                request.getCoordinates().getX() == null ||
                request.getCoordinates().getY() == null
        ) {
            throw ErrorDescriptions.COORDINATES_MUST_PRESENT.exception();
        }

        if (request.getCoordinates().getX() <= -686L) {
            throw ErrorDescriptions.X_BAD.exception();
        }

        if (request.getDiscount() == null || request.getDiscount() < 1 || request.getDiscount() > 100) {
            throw ErrorDescriptions.DISCOUNT_MUST_PRESENT.exception();
        }

        if (request.getEvent() != null && request.getEvent().getId() == 0) {
            request.getEvent().setId(null);
        }


        Ticket ticket = new Ticket();
        ticket.setName(request.getName());
        ticket.setCoordinateX(request.getCoordinates().getX());
        ticket.setCoordinateY(request.getCoordinates().getY());
        ticket.setCreationDate(new Date());
        ticket.setPrice(request.getPrice());
        ticket.setDiscount(request.getDiscount());
        ticket.setRefundable(request.getRefundable());
        ticket.setType(request.getType());

        TicketDto createdTicket = new TicketDto();

        if (request.getEvent() != null) {
            if (request.getEvent().getId() != null) {
                if (!eventRepository.existsById(request.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(request.getEvent().getId()).get();
                    ticket.setEvent(event);
                    createdTicket.setEvent(eventModelMapper.map(event));
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRestRequest.of(
                        request.getEvent().getName(),
                        request.getEvent().getDate(),
                        request.getEvent().getMinAge(),
                        request.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                ticket.setEvent(event);
                createdTicket.setEvent(eventModelMapper.map(event));
            }
        }
        ticketRepository.save(ticket);

        createdTicket.setId(ticket.getId());
        createdTicket.setName(ticket.getName());
        createdTicket.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        createdTicket.setCreationDate(Date.from(ticket.getCreationDate().toInstant()));
        createdTicket.setPrice(ticket.getPrice());
        createdTicket.setDiscount(ticket.getDiscount());
        createdTicket.setRefundable(ticket.getRefundable());
        createdTicket.setType(ticket.getType());
        return createdTicket;
    }

    @Override
    public List<TicketDto> getAllTickets(List<String> filter, String sort, Long limit, Long offset) throws Exception {
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

                    if (f.trim().equals("")) {
                        continue;
                    }
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
        List<TicketDto> tickets = getAllTickets(filters, sc, limit, offset);
        return tickets;
    }

    @Override
    public List<TicketDto> getAllTickets(
            List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset
    ) throws Exception {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        try {
            TicketSpecification spec = new TicketSpecification(filterBy);
            var ticketsStream = ticketRepository.findAll(spec).stream();

            for (var f :filterBy){
                if (f.getKey().equals("creationDate")){
                    if (f.getOperation().equals("eq")) {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        ticketsStream =ticketsStream.filter(ticket -> !ticket.getCreationDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().before((Date)f.getValue()));
                    } else {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().after((Date)f.getValue()));
                    }
                } else if (f.getKey().equals("type")){
                    if (f.getOperation().equals("eq")) {
                        ticketsStream = ticketsStream.filter(event -> event.getType().equals(f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        ticketsStream = ticketsStream.filter(event -> !event.getType().equals(f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        ticketsStream = ticketsStream.filter(event -> (event.getType().compareTo((TicketType) f.getValue()) < 0));
                    } else {
                        ticketsStream = ticketsStream.filter(event -> (event.getType().compareTo((TicketType) f.getValue()) > 0));
                    }
                }
            }

            if (sortBy != null && sortBy.size() != 0) {
                Comparator<Ticket> c = null;
                for (SortCriteria sortCriteria : sortBy) {
                    Comparator<Ticket> currentComp;
                    var desc = !sortCriteria.getAscending();
                    switch (sortCriteria.getKey()) {
                        case "id": currentComp = Comparator.comparing(Ticket::getId); break;
                        case "name": currentComp = Comparator.comparing(Ticket::getName); break;
                        case "coordinateX": currentComp = Comparator.comparing(Ticket::getCoordinateX); break;
                        case "coordinateY": currentComp = Comparator.comparing(Ticket::getCoordinateY); break;
                        case "creationDate": currentComp = Comparator.comparing(Ticket::getCreationDate); break;
                        case "price": currentComp = Comparator.comparing(Ticket::getPrice); break;
                        case "discount": currentComp = Comparator.comparing(Ticket::getDiscount); break;
                        default: throw ErrorDescriptions.INCORRECT_SORT.exception();
                    }
                    if (desc) currentComp = currentComp.reversed();
                    if (c == null) {
                        c = currentComp;
                    } else {
                        c = c.thenComparing(currentComp);
                    }
                }
                if (c != null) ticketsStream = ticketsStream.sorted(c);
            }
            return ticketsStream
                    .skip(offset)
                    .limit(limit)
                   .map(ticketModelMapper::map)
                   .collect(Collectors.toList());
        } catch (InvalidDataAccessApiUsageException exc){
            throw new Exception("В фильтре передано недопустимое значение для сравнения");
        }
    }

    @Override
    public TicketDto getTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        return ticketModelMapper.map(ticket);
    }

//    @Override
//    public TicketDto newVipTicketById(Long ticketId) {
//        if (!ticketRepository.existsById(ticketId)) {
//            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
//        }
//        Ticket ticket = ticketRepository.findById(ticketId).get();
//        System.out.println(ticket.getEvent());
//        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
//                ticket.getName(),
//                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
//                ticket.getPrice() * 2,
//                ticket.getDiscount(),
//                ticket.getRefundable(),
//                TicketType.VIP,
//                eventModelMapper.map(ticket.getEvent())
//        ));
//        return newVipTicket;
//    }

//    @Override
//    public TicketDto newDiscountTicketById(Long ticketId, Double discount) {
//        if (!ticketRepository.existsById(ticketId)) {
//            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
//        }
//        Ticket ticket = ticketRepository.findById(ticketId).get();
//        System.out.println(ticket.getEvent());
//        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
//                ticket.getName(),
//                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
//                ticket.getPrice() * (1 - discount / 100.0),
//                discount,
//                ticket.getRefundable(),
//                ticket.getType(),
//                eventModelMapper.map(ticket.getEvent())
//        ));
//        return newVipTicket;
//    }

    @Override
    @Transactional
    public void deleteTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)){
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public TicketDto updateTicketById(Long ticketId, TicketDto ticketDto) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }

        if (ticketDto.getRefundable() == null) {
            throw ErrorDescriptions.REFUNDABLE_MUST_PRESENT.exception();
        }

        if (ticketDto.getDiscount() == null || ticketDto.getDiscount() < 1 || ticketDto.getDiscount() > 100) {
            throw ErrorDescriptions.DISCOUNT_MUST_PRESENT.exception();
        }

        if (ticketDto.getCoordinates() == null ||
                ticketDto.getCoordinates().getX() == null ||
                ticketDto.getCoordinates().getY() == null
        ) {
            throw ErrorDescriptions.COORDINATES_MUST_PRESENT.exception();
        }

        if (ticketDto.getCoordinates().getX() <= -686L) {
            throw ErrorDescriptions.X_BAD.exception();
        }


        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(ticketId);
        updatedTicket.setName(ticketDto.getName());
        updatedTicket.setCoordinateX(ticketDto.getCoordinates().getX());
        updatedTicket.setCoordinateY(ticketDto.getCoordinates().getY());
        updatedTicket.setCreationDate(new Date());
        updatedTicket.setPrice(ticketDto.getPrice());
        updatedTicket.setDiscount(ticketDto.getDiscount());
        updatedTicket.setRefundable(ticketDto.getRefundable());
        updatedTicket.setType(ticketDto.getType());

        TicketDto createdTicket = new TicketDto();

        if (ticketDto.getEvent() != null) {
            if (ticketDto.getEvent().getId() != null) {
                if (!eventRepository.existsById(ticketDto.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(ticketDto.getEvent().getId()).get();
                    updatedTicket.setEvent(event);
                    createdTicket.setEvent(eventModelMapper.map(event));
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRestRequest.of(
                        ticketDto.getEvent().getName(),
                        ticketDto.getEvent().getDate(),
                        ticketDto.getEvent().getMinAge(),
                        ticketDto.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                updatedTicket.setEvent(event);
                createdTicket.setEvent(eventModelMapper.map(event));
            }
        }
        ticketRepository.save(updatedTicket);
        return ticketModelMapper.map(updatedTicket);
    }

    @Override
    public long countTickets() {
        return ticketRepository.count();
    }

    @Override
    public List<Object> getTypes() {
        var res = new ArrayList<>();
        for (var type : TicketType.values()) {
            HashMap<String, String> a = new HashMap<>();
            a.put("value", type.name());
            a.put("desc", type.toString());
            res.add(a);
        }
        return res;
    }

    @Override
    public Double sumOfDiscount() {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).map(Ticket::getDiscount).reduce((double) 0, Double::sum);
    }

    @Override
    public Object sumOfDiscountCount() {
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
        var res = new ArrayList<Map<String, Number>>();

        for(var t : groupedByDiscount.entrySet()){
            var hueta =
                    Map.of("discount", t.getKey(), "count", (Number) t.getValue().size());
            res.add(hueta);
        }
        return res;
    }

    @Override
    public Long getTicketsTypeCount(TicketType ticketType) {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .filter(ticket -> ticket.getType().getValue() < ticketType.getValue())
                .count();
    }
}
