package org.example.mapper;

import org.example.catalog.EventDto2;
import org.example.model.enums.EventType;
import org.example.model.model.EventDto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SoapEventMapper {


    public static Date fromXmlGrigDate(XMLGregorianCalendar cal) {

        return cal.toGregorianCalendar().getTime();
    }


    public static XMLGregorianCalendar toXmlGrigDate(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

    public static EventDto fromSoapToEventDto(EventDto2 e) {
        var d = e.getDate()!=null? fromXmlGrigDate(e.getDate()) : null;

        var edto = EventDto.builder()
                .id(e.getId())
                .minAge(e.getMinAge())
                .name(e.getName())
                .date(d)
                .build();

        if (e.getEventType() != null) edto.setEventType(EventType.valueOf(e.getEventType()));
        return edto;
    }

    public static EventDto2 fromEventDtoToSoap(EventDto e) throws DatatypeConfigurationException {
        EventDto2 event = new EventDto2();
        if (e.getEventType() != null){
            event.setEventType(e.getEventType().name());
        }
        if (e.getDate() != null) event.setDate(toXmlGrigDate(e.getDate()));
        event.setMinAge(e.getMinAge());
        event.setId(e.getId());
        event.setName(e.getName());
        return event;
    }
}
