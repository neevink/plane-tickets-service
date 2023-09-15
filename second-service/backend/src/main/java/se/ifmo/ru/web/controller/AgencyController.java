package se.ifmo.ru.web.controller;

import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import se.ifmo.ru.mapper.FlatMapper;
import se.ifmo.ru.service.AgencyService;
import se.ifmo.ru.service.model.Flat;
import se.ifmo.ru.utils.ResponseUtils;
import se.ifmo.ru.config.JNDIConfig;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/agency")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgencyController {

    AgencyService agencyService = JNDIConfig.agencyService();

    @Inject
    ResponseUtils responseUtils;

    @Inject
    FlatMapper flatMapper;

    public AgencyController() throws NamingException {
    }

    @GET
    @Path("/find-with-balcony/{cheapest}/{balcony}")
    public Response getFlatWithBalcony(@PathParam("cheapest") String cheapest, @PathParam("balcony") String balcony) throws JsonProcessingException{
        if ((cheapest.equals("true") || cheapest.equals("false")) && (balcony.equals("true") || balcony.equals("false"))) {

            Flat flat = unMarshalFlats(agencyService.findFlatWithBalcony(Boolean.parseBoolean(cheapest), Boolean.parseBoolean(balcony)));

            return Response.ok()
                    .entity(flatMapper.toDto(flat))
                    .build();
        }

        throw new IllegalArgumentException("Invalid parameters supplied");
    }

    @GET
    @Path("/get-most-expensive/{id1}/{id2}/{id3}")
    public long getFlatMostExpensive(@PathParam("id1") long id1, @PathParam("id2") long id2, @PathParam("id3") long id3) {
        return agencyService.getMostExpensiveFlat(id1, id2, id3);
//        return Response.ok()
//                .entity(IdResponseDto.builder().id(agencyService.getMostExpensiveFlat(id1, id2, id3)).build())
//                .build();
    }

    private Flat unMarshalFlats(String jsonFlatArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(jsonFlatArray, new TypeReference<>() {
        });
    }

}
