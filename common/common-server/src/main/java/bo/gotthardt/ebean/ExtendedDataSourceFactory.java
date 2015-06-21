package bo.gotthardt.ebean;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

@Getter
public class ExtendedDataSourceFactory extends DataSourceFactory {
    /** Whether database migrations should be performed automatically. */
    @JsonProperty("migrations")
    private boolean migrationsEnabled = true;
}
