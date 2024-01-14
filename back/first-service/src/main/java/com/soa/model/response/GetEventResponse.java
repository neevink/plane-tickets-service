package com.soa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soa.model.enums.EventType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@XmlRootElement(name="getEventResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetEventResponse {

    @XmlElement
    private EventDto2 eventDto2;
}

