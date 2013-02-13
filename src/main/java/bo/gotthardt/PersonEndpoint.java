package bo.gotthardt;

import bo.gotthardt.model.Person;

import javax.ws.rs.Path;

/**
 * @author Bo Gotthardt
 */
@Path("persons")
public class PersonEndpoint extends RestEndpoint<Person> {
    public PersonEndpoint() {
        super(Person.class);
    }
}
