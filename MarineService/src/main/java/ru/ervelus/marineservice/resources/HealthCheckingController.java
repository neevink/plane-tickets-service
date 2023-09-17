package ru.ervelus.marineservice.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckingController extends BaseController {

    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {
        return ResponseEntity.ok().build();
    }

}