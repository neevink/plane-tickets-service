package org.example.mapper;

import org.example.catalog.CoordinatesDto;
import org.example.catalog.EventDto2;
import org.example.catalog.TicketDto2;
import org.example.model.enums.EventType;
import org.example.model.enums.TicketType;
import org.example.model.model.Coordinates;
import org.example.model.model.EventDto;
import org.example.model.model.TicketDto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SoapTicketMapper {


    public static Date fromXmlGrigDate(XMLGregorianCalendar cal) {
        return cal.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar toXmlGrigDate(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

    public static TicketDto fromSoapToTicketDto(TicketDto2 t) {
        TicketDto r = new TicketDto();
        r.setId(t.getId());
        r.setPrice(t.getPrice());
        r.setName(t.getName());

        if (t.getCoordinates() != null){
            Coordinates c = new Coordinates();
            c.setX(t.getCoordinates().getX());
            c.setY(t.getCoordinates().getY());
            r.setCoordinates(c);
        }

        if (t.getCreationDate() != null) {
            r.setCreationDate(fromXmlGrigDate(t.getCreationDate()));
        }

        r.setPrice(t.getPrice());

        r.setDiscount(t.getDiscount());
        r.setRefundable(t.isRefundable());
        if (t.getType() != null) {
            r.setType(TicketType.valueOf(t.getType()));
        }

        EventDto2 event = t.getEvent();
        if (event != null) {
            var eventGood = SoapEventMapper.fromSoapToEventDto(event);
            r.setEvent(eventGood);
        }

        return r;
    }

    public static TicketDto2 fromTicketDtoToSoap(TicketDto e) throws DatatypeConfigurationException {
        TicketDto2 res = new TicketDto2();

        res.setId(e.getId());
        res.setName(e.getName());
        if (e.getCoordinates() != null) {
            CoordinatesDto c = new CoordinatesDto();
            c.setX(e.getCoordinates().getX());
            c.setY(e.getCoordinates().getY());
            res.setCoordinates(c);
        }
        if (e.getCreationDate() != null) {
            res.setCreationDate(toXmlGrigDate(e.getCreationDate()));
        }
        res.setPrice(e.getPrice());
        res.setDiscount(e.getDiscount());
        res.setRefundable(e.getRefundable());
        res.setType(e.getType().name());
        if (e.getEvent() != null) {
            var ev = e.getEvent();
            var evSoap = SoapEventMapper.fromEventDtoToSoap(ev);
            res.setEvent(evSoap);
        }

        return res;
    }
}
