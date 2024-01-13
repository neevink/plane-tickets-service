package org.example.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PingGetResponseDto {
    @NotNull(message = "id - не может быть пустым!")
    @Size(min = 1, message = "id - должен быть больше 0!")
    private Long id;
}
