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
@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDto2 {
    @XmlElement(name="id")
    private Long id;

    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @NotNull
    @XmlElement(name="name")
    private String name;
}

