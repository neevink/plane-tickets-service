package se.ifmo.ru.config;

import se.ifmo.ru.service.AgencyService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.NotFoundException;
import java.util.Properties;

public class JNDIConfig {

    public static AgencyService agencyService(){
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProps.put("jboss.naming.client.ejb.context", true);
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:46021");
        try {
            final Context context = new InitialContext(jndiProps);
            return  (AgencyService) context.lookup("ejb:/second-service-ejb/AgencyServiceImpl!se.ifmo.ru.service.AgencyService");
        } catch (NamingException e){
            System.out.println("Не получилось :(");
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
