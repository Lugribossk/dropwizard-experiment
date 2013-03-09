package bo.gotthardt.api;

import bo.gotthardt.ListFilteringProvider;
import bo.gotthardt.model.Person;
import bo.gotthardt.util.ImprovedResourceTest;
import bo.gotthardt.util.InMemoryEbeanServer;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.server.transaction.log.JuliTransactionLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bo.gotthardt.util.fest.DropwizardAssertions.assertThat;


/**
 * Tests for {@link bo.gotthardt.api.PersonEndpoint}.
 *
 * @author Bo Gotthardt
 */
public class PersonEndpointTest extends ImprovedResourceTest {
    private final EbeanServer ebean = new InMemoryEbeanServer();

    @Override
    protected void setUpResources() throws Exception {
        addResource(new PersonEndpoint(ebean));
        addProvider(ListFilteringProvider.class);

        // Squelch annoying loggers.
        // TODO move to configuration somewhere.
        Logger.getLogger("com.sun.jersey").setLevel(Level.WARNING);
        Logger.getLogger(JuliTransactionLogger.class.getName()).setLevel(Level.WARNING);
        Logger.getLogger(Ebean.class.getName()).setLevel(Level.SEVERE);
    }

    @Test
    public void shouldGetOneItem() {
        Person p = new Person("Test");
        p.save();

        assertThat(GET("/persons/" + p.getId()))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
    }

    @Test
    public void should404WhenOneItemNotFound() {
        assertThat(GET("/persons/1"))
                .hasStatus(Response.Status.NOT_FOUND)
                .hasContentType(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void shouldGetAllItems() {
        Person p1 = new Person("Test1");
        p1.save();
        Person p2 = new Person("Test2");
        p2.save();

        assertThat(GET("/persons"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p1, p2));
    }

    @Test
    public void shouldGetItemsMatchingQuery() {
        Person p1 = new Person("Test1");
        p1.save();
        Person p2 = new Person("Test2");
        p2.save();

        assertThat(GET("/persons?q=2"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetItemsAccordingToLimitAndOffest() {
        Person p1 = new Person("Test1");
        p1.save();
        Person p2 = new Person("Test2");
        p2.save();
        Person p3 = new Person("Test3");
        p3.save();

        assertThat(GET("/persons?limit=1&offset=1"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetAllItemsWhenLimitIs0() {
        Person p1 = new Person("Test1");
        p1.save();
        Person p2 = new Person("Test2");
        p2.save();
        Person p3 = new Person("Test3");
        p3.save();

        assertThat(GET("/persons?limit=0&offset=1"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p1, p2, p3));
    }

    @Test
    public void shouldPersistPostedItem() {
        ObjectNode input = createObjectNode();
        input.put("name", "Test1");

        assertThat(POST("/persons", input))
                .hasStatus(Response.Status.OK);

        List<Person> savedItems = Ebean.find(Person.class).findList();
        assertThat(savedItems).hasSize(1);
        assertThat(savedItems.get(0).getName()).isEqualTo("Test1");
    }

//    @Test
//    public void shouldRejectInvalidPostedItem() {
//        ObjectNode input = createObjectNode();
//        input.putNull("name");
//
//        assertThat(client().resource("/persons").entity(input, MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class))
//                .hasStatus(Response.Status.OK);
//
//        assertThat(Ebean.find(Person.class).findList()).isEmpty();
//    }

    @Test
    public void shouldUpdatePuttedItem() {
        Person p = new Person("Test1");
        p.save();

        ObjectNode input = createObjectNode();
        input.put("name", "Test2");

        ClientResponse response = PUT("/persons/" + p.getId(), input);
        p.refresh();

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getName()).isEqualTo("Test2");
    }

    @Test
    public void shouldNotOverwriteIdFromPuttedItem() {
        Person p = new Person("Test1");
        p.save();
        long oldId = p.getId();

        ObjectNode input = new ObjectMapper().createObjectNode();
        input.put("name", "Test2");
        input.put("id", oldId + 100);

        ClientResponse response = PUT("/persons/" + oldId, input);
        p.refresh();

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getId()).isEqualTo(oldId);
    }

}
