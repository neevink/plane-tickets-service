package se.ifmo.ru.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.collections4.CollectionUtils;
import se.ifmo.ru.external.client.CatalogRestClient;
import se.ifmo.ru.mapper.FlatMapper;
import org.jboss.ejb3.annotation.Pool;
import se.ifmo.ru.service.model.Flat;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Stateless
@Pool("slsb-strict-max-pool")
public class AgencyServiceImpl implements AgencyService {
    @EJB
    private CatalogRestClient catalogRestClient;
    @Inject
    private FlatMapper flatMapper;

    @Override
    public String findFlatWithBalcony(boolean cheapest, boolean balcony) throws JsonProcessingException {

        List<Flat> flats = flatMapper.fromRestClient(catalogRestClient.getAllFlats());

        Flat bestFlat = new Flat();

        try {
            if (CollectionUtils.isNotEmpty(flats)) {
                Double price = 0.0;
                int i = 0;
                for (Flat flat : flats) {
                    if (flat.getBalcony() == balcony && i == 0) {
                        price = flat.getPrice();
                        i++;
                    }
                    if (cheapest) {
                        if (flat.getBalcony() == balcony && price <= flat.getPrice()) {
                            bestFlat = flat;
                        }
                    } else {
                        if (flat.getBalcony() == balcony && price >= flat.getPrice()) {
                            bestFlat = flat;
                        }
                    }
                }
            } else {
                throw new NotFoundException("Not found list of flats!");
            }

            if (bestFlat.getId() == null) {
                throw new NotFoundException("No found suitable flat!");
            }
        } catch (NotFoundException e) {
            throw new EJBException(e.getMessage());
        }

        return marshalAnswer(bestFlat);
    }

    @Override
    public long getMostExpensiveFlat(long id1, long id2, long id3) {

        Flat flat1 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id1));
        try {
            if (flat1 == null) {
                throw new NotFoundException("Flat with id " + id1 + " not found");
            }
        } catch (NotFoundException e) {
            throw new EJBException(e.getMessage());
        }

        Flat flat2 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id2));
        try {
            if (flat2 == null) {
                throw new NotFoundException("Flat with id " + id2 + " not found");
            }
        } catch (NotFoundException e) {
            throw new EJBException(e.getMessage());
        }

        Flat flat3 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id3));

        try {
            if (flat3 == null) {
                throw new NotFoundException("Flat with id " + id3 + " not found");
            }
        } catch (NotFoundException e) {
            throw new EJBException(e.getMessage());
        }

        return flat1.getPrice() >= flat2.getPrice() ?
                flat1.getPrice() >= flat3.getPrice() ?
                        flat1.getId() : flat3.getId() :
                flat2.getPrice() >= flat3.getPrice() ?
                        flat2.getId() : flat3.getId();
    }

    private String marshalAnswer(Flat flat) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flat);
    }
}
