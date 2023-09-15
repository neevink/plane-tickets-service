package se.ifmo.ru.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import se.ifmo.ru.service.model.Flat;

import javax.ejb.Remote;
import javax.ws.rs.NotFoundException;

@Remote
public interface AgencyService {

    String findFlatWithBalcony(boolean cheapest, boolean balcony) throws JsonProcessingException;

    long getMostExpensiveFlat(long id1, long id2, long id3);
}
