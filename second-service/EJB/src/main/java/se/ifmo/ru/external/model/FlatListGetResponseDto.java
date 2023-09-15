package se.ifmo.ru.external.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class FlatListGetResponseDto {
    private List<RestClientFlat> flatGetResponseDtos;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCount;

    public List<RestClientFlat> getFlatGetResponseDtos () {
        return flatGetResponseDtos;
    }

    public FlatListGetResponseDto(List<RestClientFlat> flatGetResponseDtos, Integer page, Integer pageSize, Integer totalPages, Long totalCount){
        this.flatGetResponseDtos = flatGetResponseDtos;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalCount = totalCount;
    }
}
