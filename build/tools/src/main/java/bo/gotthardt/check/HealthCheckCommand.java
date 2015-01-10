package bo.gotthardt.check;

import com.mashape.unirest.http.Unirest;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.util.concurrent.TimeUnit;

/**
 * @author Bo Gotthardt
 */
public class HealthCheckCommand extends Command {
    public HealthCheckCommand() {
        super("health", "Healthcheck");
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        String url = namespace.getString("url");

        boolean healthy = new HealthChecker(url, namespace.getLong("duration")).check();

        if (!healthy) {
            System.exit(-1);
        }
        Unirest.shutdown();
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("--url")
                .required(true)
                .help("Application URL.");

        subparser.addArgument("--duration")
                .setDefault(TimeUnit.MINUTES.toSeconds(1))
                .help("Maximum number of seconds to wait.");
    }
}
