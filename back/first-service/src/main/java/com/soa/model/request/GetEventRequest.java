package com.soa.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name="getEventRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetEventRequest {
    @XmlElement(name="id")
    private Long id;
}
