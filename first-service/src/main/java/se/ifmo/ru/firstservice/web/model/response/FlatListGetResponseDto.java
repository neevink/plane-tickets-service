package se.ifmo.ru.firstservice.web.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class FlatListGetResponseDto {
    private List<FlatGetResponseDto> flatGetResponseDtos;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCount;

    public List<FlatGetResponseDto> getFlatGetResponseDtos () {
        return flatGetResponseDtos;
    }

    public FlatListGetResponseDto(List<FlatGetResponseDto> flatGetResponseDtos, Integer page, Integer pageSize, Integer totalPages, Long totalCount){
        this.flatGetResponseDtos = flatGetResponseDtos;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalCount = totalCount;
    }
}
