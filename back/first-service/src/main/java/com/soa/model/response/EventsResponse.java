package com.soa.model.response;

import com.soa.model.EventDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsResponse {
    public List<EventDto> events;
}
