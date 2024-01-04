package com.soa;

import com.soa.model.EventDto2;
import com.soa.model.GetEventResponse;
import com.soa.model.request.GetEventRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

@SpringBootApplication
//@EnableDiscoveryClient
public class SoaApplication {
    public static void main(String[] args) throws JAXBException {
        EventDto2 book = EventDto2.of(1L, "hello");
        var a =GetEventResponse.of(book);

        JAXBContext context = JAXBContext.newInstance(GetEventResponse.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(a, new File("./book.xml"));

        System.out.println("hello");



        GetEventRequest req = new GetEventRequest();
        req.setId(1L);
        JAXBContext context1 = JAXBContext.newInstance(GetEventRequest.class);
        Marshaller mar1= context1.createMarshaller();
        mar1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar1.marshal(req, new File("./book1.xml"));

        System.out.println("hello");

        SpringApplication.run(SoaApplication.class, args);
    }

}
