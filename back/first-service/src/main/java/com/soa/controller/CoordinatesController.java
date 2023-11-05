package com.soa.controller;

import com.soa.entity.GroupsCount;
import com.soa.service.CoordinatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/labworks/coordinates")
@AllArgsConstructor
public class CoordinatesController {
    private CoordinatesService coordinatesService;

    @GetMapping("/grouped")
    public ResponseEntity<List<GroupsCount>> getGroupedByCoordinates() {
        return ResponseEntity.status(200).body(coordinatesService.getGroupedByCoordinates());
    }
}
