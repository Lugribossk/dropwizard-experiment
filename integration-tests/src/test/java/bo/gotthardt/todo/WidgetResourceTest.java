package bo.gotthardt.todo;

import bo.gotthardt.jersey.parameters.ListFilteringFactory;
import bo.gotthardt.model.Widget;
import bo.gotthardt.rest.CrudService;
import bo.gotthardt.test.ApiIntegrationTest;
import bo.gotthardt.test.ResourceTestRule2;
import bo.gotthardt.todolist.rest.WidgetResource;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
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
    private static final CrudService<Widget> service = new CrudService<>(Widget.class, db);
    @ClassRule
    public static final ResourceTestRule2 resources = ResourceTestRule2.builder()
            .addResource(new WidgetResource(service))
            .addResource(ListFilteringFactory.getBinder())
            .build();

    @Test
    public void shouldGetOneItem() {
        Widget p = new Widget("Test");
        db.save(p);

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
        db.save(p1);
        Widget p2 = new Widget("Test2");
        db.save(p2);

        assertThat(GET("/widgets"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p1, p2));
    }

    @Test
    public void shouldGetItemsMatchingQuery() {
        Widget p1 = new Widget("Test1");
        db.save(p1);
        Widget p2 = new Widget("Test2");
        db.save(p2);

        assertThat(GET("/widgets?q=2"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetItemsAccordingToLimitAndOffest() {
        Widget p1 = new Widget("Test1");
        db.save(p1);
        Widget p2 = new Widget("Test2");
        db.save(p2);
        Widget p3 = new Widget("Test3");
        db.save(p3);

        assertThat(GET("/widgets?limit=1&offset=1"))
                .hasStatus(Response.Status.OK)
                .hasJsonContent(ImmutableList.of(p2));
    }

    @Test
    public void shouldGetAllItemsWhenLimitIs0() {
        Widget p1 = new Widget("Test1");
        db.save(p1);
        Widget p2 = new Widget("Test2");
        db.save(p2);
        Widget p3 = new Widget("Test3");
        db.save(p3);

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

        List<Widget> savedItems = db.find(Widget.class).findList();
        assertThat(savedItems).hasSize(1);
        assertThat(savedItems.get(0).getName()).isEqualTo("Test1");
    }

    @Test
    public void shouldUpdatePuttedItem() {
        Widget p = new Widget("Test1");
        db.save(p);

        ObjectNode input = resources.getObjectMapper().createObjectNode();
        input.put("name", "Test2");

        Response response = PUT("/widgets/" + p.getId(), input);
        db.refresh(p);

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getName()).isEqualTo("Test2");
    }

    @Test
    public void shouldNotOverwriteIdFromPuttedItem() {
        Widget p = new Widget("Test1");
        db.save(p);
        long oldId = p.getId();

        ObjectNode input = resources.getObjectMapper().createObjectNode();
        input.put("name", "Test2");
        input.put("id", oldId + 100);

        Response response = PUT("/widgets/" + oldId, input);
        db.refresh(p);

        assertThat(response)
                .hasStatus(Response.Status.OK)
                .hasJsonContent(p);
        assertThat(p.getId()).isEqualTo(oldId);
    }

    @Override
    public ResourceTestRule2 getResources2() {
        return resources;
    }
}
