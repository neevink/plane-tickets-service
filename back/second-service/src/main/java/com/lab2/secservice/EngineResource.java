package com.lab2.secservice;

import com.lab2.secservice.services.EngineService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/search/by-engine-power/")
public class EngineResource {
    @Inject
    EngineService engineService;

    @GET
    @Path("{from}/{to}")
    public Response search(@PathParam("from") String from,
                           @PathParam("to") String to) {

        return Response.status(200)
                .entity(engineService.getVehiclesByEngine(from, to))
                .build();

    }
}
