package com.soa.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PageService {
    private final int DEFAULT_LIMIT = 3;
    private final int DEFAULT_OFFSET = 0;

    private SortService sortService;

    public PageRequest getPageRequest(Integer limitParam, Integer offsetParam, boolean sortAsc, List<String> sortingFields) {
        int limit = limitParam == null ? DEFAULT_LIMIT : limitParam;
        int offset = offsetParam == null ? DEFAULT_OFFSET : offsetParam;
        int page = offset / limit;
        List<String> resultSort = new ArrayList<>();
        for (String s : sortingFields) {
            if (s.equals("x")) {
                resultSort.add("coordinates.x");
            } else if (s.equals("y")) {
                resultSort.add("coordinates.y");
            } else if (s.equals("disciplineName")) {
                resultSort.add("discipline.name");
            } else if (s.equals("selfStudyHours")) {
                resultSort.add("discipline.selfStudyHours");
            } else {
                resultSort.add(s);
            }
        }
        String sorts = resultSort.stream().map(Object::toString).collect(Collectors.joining(","));
        return sorts.isEmpty() ? PageRequest.of(page, limit)
                : PageRequest.of(page, limit, JpaSort.unsafe(sortService.getSort(sortAsc), sorts));
    }
}
