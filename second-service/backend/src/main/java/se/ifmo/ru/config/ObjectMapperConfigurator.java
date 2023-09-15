package se.ifmo.ru.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class ObjectMapperConfigurator {
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectMapper createObjectMapper(){
        return new ObjectMapper();
    }
}