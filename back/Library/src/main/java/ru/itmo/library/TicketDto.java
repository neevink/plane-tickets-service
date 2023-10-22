package ru.itmo.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.library.enums.TicketType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TicketDto {
    private Long id;
    private String name;
    private Coordinates coordinates;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date creationDate;
    private Double price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    private Long eventId;

}

