package se.ifmo.ru.firstservice.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private String fieldName;
    private String nestedName;
    private FilteringOperation filteringOperation;
    private String fieldValue;
}
