package bo.gotthardt.todo;

import bo.gotthardt.jersey.provider.ListFilteringProvider;
import bo.gotthardt.model.Widget;
import bo.gotthardt.todolist.rest.WidgetResource;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.test.ApiIntegrationTest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;


/**
 * Tests for {@link bo.gotthardt.todolist.rest.WidgetResource}.
 *
 * @author Bo Gotthardt
 */
public class WidgetResourceTest extends ApiIntegrationTest {
    private static final CrudService<Widget> service = new CrudService<>(Widget.class, ebean);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new WidgetResource(service))
            .addResource(new ListFilteringProvider())
            .build();

    @Test
    public void shouldGetOneItem() {
        Widget p = new Widget("Test");
        ebean.save(p);

        assertThat(GET("/widgets/" + p.getId()))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
    }

    @Test
    public void should404WhenOneItemNotFound() {
        assertThat(GET("/widgets/1"))
                .hasStatus(Response.Status.NOT_FOUND)
                .hasContentType(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void shouldGetAllItems() {
        Widget p1 = new Widget("Test1");
        ebean.save(p1);
        Widget p2 = new Widget("Test2");
        ebean.save(p2);

        assertThat(GET("/widgets"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p1, p2));
    }

    @Test
    public void shouldGetItemsMatchingQuery() {
        Widget p1 = new Widget("Test1");
        ebean.save(p1);
        Widget p2 = new Widget("Test2");
        ebean.save(p2);

        assertThat(GET("/widgets?q=2"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetItemsAccordingToLimitAndOffest() {
        Widget p1 = new Widget("Test1");
        ebean.save(p1);
        Widget p2 = new Widget("Test2");
        ebean.save(p2);
        Widget p3 = new Widget("Test3");
        ebean.save(p3);

        assertThat(GET("/widgets?limit=1&offset=1"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetAllItemsWhenLimitIs0() {
        Widget p1 = new Widget("Test1");
        ebean.save(p1);
        Widget p2 = new Widget("Test2");
        ebean.save(p2);
        Widget p3 = new Widget("Test3");
        ebean.save(p3);

        assertThat(GET("/widgets?limit=0&offset=1"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p1, p2, p3));
    }

    @Test
    public void shouldPersistPostedItem() {
        ObjectNode input = resources.getObjectMapper().createObjectNode();
        input.put("name", "Test1");

        assertThat(POST("/widgets", input))
                .hasStatus(Response.Status.OK);

        List<Widget> savedItems = ebean.find(Widget.class).findList();
        assertThat(savedItems).hasSize(1);
        assertThat(savedItems.get(0).getName()).isEqualTo("Test1");
    }

    @Test
    public void shouldUpdatePuttedItem() {
        Widget p = new Widget("Test1");
        ebean.save(p);

        ObjectNode input = resources.getObjectMapper().createObjectNode();
        input.put("name", "Test2");

        ClientResponse response = PUT("/widgets/" + p.getId(), input);
        ebean.refresh(p);

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getName()).isEqualTo("Test2");
    }

    @Test
    public void shouldNotOverwriteIdFromPuttedItem() {
        Widget p = new Widget("Test1");
        ebean.save(p);
        long oldId = p.getId();

        ObjectNode input = resources.getObjectMapper().createObjectNode();
        input.put("name", "Test2");
        input.put("id", oldId + 100);

        ClientResponse response = PUT("/widgets/" + oldId, input);
        ebean.refresh(p);

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getId()).isEqualTo(oldId);
    }

    @Override
    public ResourceTestRule getResources() {
        return resources;
    }
}
