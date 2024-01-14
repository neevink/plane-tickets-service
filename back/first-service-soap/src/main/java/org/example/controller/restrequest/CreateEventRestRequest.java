package org.example.controller.restrequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.EventType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateEventRestRequest {
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
    private Integer minAge;
    private EventType eventType;
}
