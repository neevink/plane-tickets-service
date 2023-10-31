package com.lab2.secservice.services;

import com.lab2.secservice.RestClient;
import com.lab2.secservice.dto.VehicleDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class EngineService {
    @Inject
    private RestClient restClient;

    public List<VehicleDTO> getVehiclesByEngine(String from, String to) {
        return restClient.getVehiclesBetween(from, to);
    }
}
