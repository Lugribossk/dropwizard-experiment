package bo.gotthardt.ebean;

import com.avaje.ebean.config.DataSourceConfig;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * @author Bo Gotthardt
 */
@Getter
@Setter
public class EbeanConfiguration {
    @Valid
    private String username;
    @Valid
    private String password;
    @NotEmpty
    private String url;
    @NotEmpty
    private String driver;

    public DataSourceConfig toDataSourceConfig() {
        DataSourceConfig config = new DataSourceConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setUrl(url);
        config.setDriver(driver);
        return config;
    }
}
