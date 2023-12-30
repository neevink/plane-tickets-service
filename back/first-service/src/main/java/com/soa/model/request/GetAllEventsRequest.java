package com.soa.model.request;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class GetAllEventsRequest {
    String[] filter;
    String sort;
    Long limit;
    Long offset;
}
