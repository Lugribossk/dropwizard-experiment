package bo.gotthardt.api;

import bo.gotthardt.model.Person;
import com.avaje.ebean.EbeanServer;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("/persons")
public class PersonEndpoint extends RestEndpoint<Person> {
    public PersonEndpoint(EbeanServer ebean) {
        super(Person.class, ebean);
    }
}
