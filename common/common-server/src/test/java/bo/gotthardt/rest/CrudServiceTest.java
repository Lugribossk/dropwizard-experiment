package bo.gotthardt.rest;

import bo.gotthardt.Persistable;
import bo.gotthardt.exception.NotFoundException;
import com.avaje.ebean.EbeanServer;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.UUID;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link bo.gotthardt.rest.CrudService}.
 *
 * @author Bo Gotthardt
 */
public class CrudServiceTest {
    private EbeanServer db = mock(EbeanServer.class);
    private CrudService<TestItem> service = new CrudService<>(TestItem.class, db);

    @Test
    public void fetchShouldFindInDatabase() {
        UUID id = UUID.randomUUID();
        TestItem item = new TestItem();
        when(db.find(TestItem.class, id)).thenReturn(item);

        assertThat(service.fetchById(id)).isEqualTo(item);
    }

    @Test(expected = NotFoundException.class)
    public void fetchShouldThrowWhenIdNotFound() {
        service.fetchById(UUID.randomUUID());
    }

    @Getter
    @Setter
    private static class TestItem implements Persistable {
        private UUID id;
    }
}
