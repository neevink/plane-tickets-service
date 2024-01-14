package org.example.model.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.example.model.enums.EventType;

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
@Builder
@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDto {
    @XmlElement(name="id")
    private Long id;

    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @NotNull
    @XmlElement(name="name")
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    @XmlElement(name="date")
    private Date date;

    @XmlElement(name="minAge")
    private Integer minAge;

    @XmlElement(name="eventType")
    private EventType eventType;
}

