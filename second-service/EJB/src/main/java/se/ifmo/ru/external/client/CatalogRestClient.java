package se.ifmo.ru.external.client;

import org.jboss.ejb3.annotation.Pool;
import se.ifmo.ru.external.model.FlatListGetResponseDto;
import se.ifmo.ru.external.model.RestClientFlat;

import javax.ejb.Stateless;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Pool("slsb-strict-max-pool")
public class CatalogRestClient {
    private Client client;
    private final String serviceUrl = "https://localhost:4500/api/v1";

    public RestClientFlat getFlatById(long id){
        String url = serviceUrl + "/catalog/flats/" + id;
        try {
            client = ClientBuilder.newClient();

            Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

            RestClientFlat flat = response.readEntity(RestClientFlat.class);

            client.close();

            return flat.getId() == null ? null : flat;

        } catch (ProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<RestClientFlat> getAllFlats(){
        String url = serviceUrl + "/catalog/flats";

        try {
            client = ClientBuilder.newClient();

            Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

            FlatListGetResponseDto flats = response.readEntity(FlatListGetResponseDto.class);

            client.close();

            return flats.getFlatGetResponseDtos();
        } catch (ProcessingException e){
            e.printStackTrace();
            return null;
        }
    }
}
