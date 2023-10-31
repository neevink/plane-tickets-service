package com.lab2.secservice;

import com.lab2.secservice.services.EngineService;
import com.lab2.secservice.services.WhellsService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/")
public class WhellsResource {

    @Inject
    WhellsService whellsService;

    @GET
    @Path("add-wheels/{id}/{count}")
    public Response search(@PathParam("id") String id,
                           @PathParam("count") String count) {

        return Response.status(200)
                .entity(whellsService.setWhells(id, count))
                .build();

    }
}
