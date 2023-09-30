package com.ervelus.marineservice.resources;

import com.ervelus.marineservice.service.SpaceMarineCrudService;
import ru.egormit.library.PageDto;
import ru.egormit.library.SpaceMarineCreateRequest;
import ru.egormit.library.SpaceMarineUpdateRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/spacemarines")
public class SpaceMarineCrudResource {
    @Inject
    private SpaceMarineCrudService crudService;

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public Response getAllSpaceMarines(PageDto pageDto) {
        return Response.ok().entity(crudService.getAllSpaceMarines(pageDto)).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getSpaceMarine(@PathParam("id") Long id) {
        return Response.ok().entity(crudService.getSpaceMarineById(id)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createSpaceMarine(SpaceMarineCreateRequest request) {
        crudService.createSpaceMarine(request);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response deleteSpaceMarine(@PathParam("id") Long id) {
        crudService.deleteSpaceMarine(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Produces("application/json")
    public Response updateSpaceMarine(@PathParam("id") Long id, SpaceMarineUpdateRequest request) {
        crudService.updateSpaceMarine(id, request);
        return Response.ok().build();
    }
}