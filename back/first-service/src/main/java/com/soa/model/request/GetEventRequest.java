package com.soa.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name="getEventRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id"
})
public class GetEventRequest {
    @XmlElement(name="id")
    private Long id;
}
