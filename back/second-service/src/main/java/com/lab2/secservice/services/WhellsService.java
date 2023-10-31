package com.lab2.secservice.services;

import com.lab2.secservice.RestClient;
import com.lab2.secservice.dto.VehicleDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class WhellsService {
    @Inject
    private RestClient restClient;

    public VehicleDTO setWhells(String from, String to) {
        return restClient.setWhells(from, to);
    }
}
