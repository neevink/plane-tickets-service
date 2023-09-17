package ru.ervelus.marineservice.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egormit.library.PageDto;
import ru.egormit.library.SpaceMarineCreateRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;
import ru.ervelus.marineservice.service.SpaceMarineCrudService;

@RestController
@RequestMapping(value = "/spacemarines", produces = "application/json")
@RequiredArgsConstructor
public class SpaceMarineCrudController extends BaseController {
    private final SpaceMarineCrudService crudService;

    @GetMapping
    public ResponseEntity<SpaceMarineSearchResponse> getAllSpaceMarines(@RequestBody PageDto pageDto) {
        return ResponseEntity.ok(crudService.getAllSpaceMarines(pageDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMarineResponse> getSpaceMarine(@PathVariable Long id) {
        return ResponseEntity.ok(crudService.getSpaceMarineById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSpaceMarine(@RequestBody SpaceMarineCreateRequest request) {
        crudService.createSpaceMarine(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceMarine(@PathVariable Long id) {
        crudService.deleteSpaceMarine(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpaceMarine(@PathVariable Long id, @RequestBody SpaceMarineUpdateRequest request) {
        crudService.updateSpaceMarine(id, request);
        return ResponseEntity.ok().build();
    }
}