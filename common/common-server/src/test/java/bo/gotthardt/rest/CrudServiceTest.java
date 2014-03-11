package bo.gotthardt.rest;

import bo.gotthardt.Persistable;
import bo.gotthardt.exception.NotFoundException;
import com.avaje.ebean.EbeanServer;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static bo.gotthardt.test.assertj.DropwizardAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link bo.gotthardt.rest.CrudService}.
 *
 * @author Bo Gotthardt
 */
public class CrudServiceTest {
    private EbeanServer ebean = mock(EbeanServer.class);
    private CrudService<TestItem> service = new CrudService<>(TestItem.class, ebean);

    @Test
    public void fetchShouldFindInDatabase() {
        TestItem item = new TestItem();
        when(ebean.find(TestItem.class, 1L)).thenReturn(item);

        assertThat(service.fetchById(1)).isEqualTo(item);
    }

    @Test(expected = NotFoundException.class)
    public void fetchShouldThrowWhenIdNotFound() {
        service.fetchById(1);
    }

    @Getter
    @Setter
    private static class TestItem implements Persistable {
        private long id;
    }
}
