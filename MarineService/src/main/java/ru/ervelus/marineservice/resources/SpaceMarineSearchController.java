package ru.ervelus.marineservice.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egormit.library.SpaceMarineFilterRequest;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.ervelus.marineservice.service.SpaceMarineSearchService;

@RestController
@RequestMapping(value = "/spacemarines/search", produces = "application/json")
@RequiredArgsConstructor
public class SpaceMarineSearchController extends BaseController {
    private final SpaceMarineSearchService searchService;

    @PostMapping
    public ResponseEntity<SpaceMarineSearchResponse> filterSpaceMarine(@RequestBody SpaceMarineFilterRequest request) {
        return ResponseEntity.ok(searchService.findAllSpaceMarineByFilter(request));
    }

    @PostMapping("/name")
    public ResponseEntity<SpaceMarineSearchResponse> getSpaceMarinesByName(@RequestBody SpaceMarineFilterRequest request) {
        return ResponseEntity.ok(searchService.findAllSpaceMarineByName(request));
    }

    @PostMapping("/health/greater")
    public ResponseEntity<SpaceMarineSearchResponse> getSpaceMarinesWithHealthGreaterThan(@RequestBody SpaceMarineFilterRequest request) {
        return ResponseEntity.ok(searchService.findAllSpaceMarineWithHealthGreaterThan(request));
    }
}
