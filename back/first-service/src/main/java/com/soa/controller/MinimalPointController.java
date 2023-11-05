package com.soa.controller;

import com.soa.service.MinimalPointService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/labworks/minimal-point")
@AllArgsConstructor
public class MinimalPointController {
    private MinimalPointService minimalPointService;

    @GetMapping("/sum")
    public ResponseEntity<Integer> getSum() {
        return ResponseEntity.status(200).body(minimalPointService.getSum());
    }

    @GetMapping("/unique")
    public ResponseEntity<List<Integer>> getDistinctByMinimalPoint() {
        return ResponseEntity.status(200).body(minimalPointService.getDistinctByMinimalPoint());
    }
}
