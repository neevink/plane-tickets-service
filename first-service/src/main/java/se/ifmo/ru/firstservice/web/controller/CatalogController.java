package se.ifmo.ru.firstservice.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import se.ifmo.ru.firstservice.mapper.FlatMapper;
import se.ifmo.ru.firstservice.service.api.FlatService;
import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.FlatEntity;
import se.ifmo.ru.firstservice.storage.model.Page;
import se.ifmo.ru.firstservice.util.ResponseUtils;
import se.ifmo.ru.firstservice.web.model.request.FlatAddOrUpdateRequestDto;
import se.ifmo.ru.firstservice.web.model.response.FlatGetResponseDto;
import se.ifmo.ru.firstservice.web.model.response.FlatListGetResponseDto;
import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.firstservice.web.model.Error;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final FlatService flatService;
    private final FlatMapper flatMapper;
    private final ResponseUtils responseUtils;

    @Autowired
    public CatalogController(FlatService flatService, FlatMapper flatMapper, ResponseUtils responseUtils){
        this.flatService = flatService;
        this. responseUtils = responseUtils;
        this.flatMapper = flatMapper;
    }

    @GetMapping("/flats")
    public ResponseEntity<?> getFlats(final HttpServletRequest request){
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");

        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        Integer page = null, pageSize = null;

        try {
            if (StringUtils.isNotEmpty(pageParam)) {
                page = Integer.parseInt(pageParam);
                if (page <= 0) {
                    throw new NumberFormatException();
                }
            }
            if (StringUtils.isNotEmpty(pageSizeParam)) {
                pageSize = Integer.parseInt(pageSizeParam);
                if (pageSize <= 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException numberFormatException) {
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Invalid query param value");
        }

        List<String> sort = sortParameters == null
                ? new ArrayList<>()
                : Stream.of(sortParameters).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> filter = filterParameters == null
                ? new ArrayList<>()
                : Stream.of(filterParameters).filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        Page<Flat> resultPage = flatService.getFlats(sort, filter, page, pageSize);

        if (resultPage == null) {
            return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, "Not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FlatListGetResponseDto(
                        flatMapper.toGetResponseDtoList(resultPage.getObjects()),
                        resultPage.getPage(),
                        resultPage.getPageSize(),
                        resultPage.getTotalPages(),
                        resultPage.getTotalCount()
                ));
    }

    @GetMapping("/flats/{id}")
    public ResponseEntity<?> getFlat(@PathVariable("id") long id){
        Flat flat = flatService.getFlat(id);

        if (flat == null){
            return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, "Flat with id " + id + " not found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(flatMapper.toDto(flat));
    }

    @PostMapping("/flats")
    public ResponseEntity<?> addFlat(@Valid @RequestBody FlatAddOrUpdateRequestDto requestDto) {
        Flat flat = flatService.addFlat(requestDto);

        return ResponseEntity.ok()
                .body(flatMapper.toDto(flat));
    }

    @PutMapping("/flats/{id}")
    public ResponseEntity<?> updateFlate(@PathVariable("id") long id, @Valid @RequestBody FlatAddOrUpdateRequestDto requestDto){
        Flat flat = flatService.updateFlat(id, requestDto);

        if (flat == null){
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Flat with id " + id + " not found!");
        }

        return ResponseEntity.ok()
                .body(flatMapper.toDto(flat));
    }

    @DeleteMapping("/flats/{id}")
    public ResponseEntity<?> deleteFlat(@PathVariable("id") long id){
        boolean deleted = flatService.deleteFlat(id);

        if (!deleted){
            return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, "Flat with id " + id + " not found");
        }

        return responseUtils.buildResponseWithMessage(HttpStatus.NO_CONTENT, "Flat with id " + id + " was successfully deleted");
    }

    @DeleteMapping("flats/delete-one-by-view/{view}")
    public ResponseEntity<?> deleteFlatByView(@PathVariable("view") String view){
        boolean deleted = flatService.deleteOneFlatByView(View.fromValue(view.toLowerCase()));

        if (!deleted){
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Flat with view " + view + " not found");
        }

        return responseUtils.buildResponseWithMessage(HttpStatus.NO_CONTENT, "One Flat with view " + View.fromValue(view.toLowerCase()).toString() + " was successfully deleted");
    }

    @GetMapping("flats/average-time-metro")
    public ResponseEntity<?> getFlatsAverageTimeMetro(){
        return ResponseEntity.ok()
                .body(flatService.getFlatsAverageTimeToMetroOnFoot());
    }

    @GetMapping("flats/unique-view")
    public ResponseEntity<?> getUniqueFlatsView(){
        return ResponseEntity.ok()
                .body(flatService.getUniqueFlatsView());
    }
}
