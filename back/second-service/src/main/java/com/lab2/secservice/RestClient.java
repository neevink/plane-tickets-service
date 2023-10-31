package com.lab2.secservice;

import com.lab2.secservice.dto.VehicleDTO;
import com.lab2.secservice.dto.VehiclesListDTO;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Stateless
public class RestClient {
    private Client client;
    private final String serviceUrl = "https://localhost:8082/vehicles";

    public List<VehicleDTO> getVehiclesBetween(String from, String to) {
        String url = serviceUrl + "/new?limit=100&minEnginePower=" + from + "&maxEnginePower=" + to;
        System.out.println(url);
        try {
            client = ClientBuilder.newClient();

            VehiclesListDTO response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get(VehiclesListDTO.class);



            client.close();

            return response.getVehiclesGetResponseDtos();

        } catch (ProcessingException e) {
            client.close();
            return null;
        }
    }

    public VehicleDTO setWhells(String id, String count) {
        String url = serviceUrl + "/" + id + "/add-wheels/" + count;
        System.out.println(url);
        try {
            client = ClientBuilder.newClient();

            Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
            System.out.println(response);
            VehicleDTO vh = response.readEntity(VehicleDTO.class);

            client.close();

            return vh;

        } catch (ProcessingException e) {
            client.close();
            return null;
        }
    }

}
