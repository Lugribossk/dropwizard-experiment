package bo.gotthardt.morphia;

import com.mongodb.DBPort;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class MongoConfiguration {
    private String host;
    private int port = DBPort.PORT;
}
