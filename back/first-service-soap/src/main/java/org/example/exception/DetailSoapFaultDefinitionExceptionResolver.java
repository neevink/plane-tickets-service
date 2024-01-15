package org.example.exception;

import org.example.error.ApplicationException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        logger.warn("Exception processed ", ex);


        if (ex instanceof ApplicationException) {
            ApplicationException e = (ApplicationException ) ex;
            int code = e.getErrorResponse().getCode();
//            String message = e.getErrorResponse().getMessage();
            SoapFaultDetail detail = fault.addFaultDetail();

            detail.addFaultDetailElement(CODE).addText(""+code);
//            detail.addFaultDetailElement(DESCRIPTION).addText(message);
        }
    }

}