package se.ifmo.ru.firstservice.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sort {
    private boolean desc;
    private String fieldName;
    private String nestedName;
}
