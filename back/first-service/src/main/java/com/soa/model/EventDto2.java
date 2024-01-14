package com.soa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soa.model.enums.EventType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventDto", propOrder = {
        "id", "name"
})
public class EventDto2 {
    @XmlElement(name="id")
    private Long id;

    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @NotNull
    @XmlElement(name="name")
    private String name;
}

