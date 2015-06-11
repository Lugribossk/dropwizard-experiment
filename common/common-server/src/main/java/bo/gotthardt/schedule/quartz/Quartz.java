package bo.gotthardt.schedule.quartz;

import com.google.common.base.Preconditions;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.lifecycle.Managed;
import org.glassfish.hk2.api.ServiceLocator;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RAMJobStore;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Properties;

public class Quartz implements Managed, Provider<Scheduler> {
    private final QuartzConfiguration quartzConfig;
    private final DataSourceFactory dbConfig;

    private Scheduler scheduler;

    @Inject
    public Quartz(QuartzConfiguration quartzConfig, DataSourceFactory dbConfig, ServiceLocator locator) throws SchedulerException {
        this.quartzConfig = quartzConfig;
        this.dbConfig = dbConfig;

        SchedulerFactory schedulerFactory = new StdSchedulerFactory(getProperties());
        scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(new HK2JobFactory(locator));

        scheduler.start();
    }

    @Override
    public void start() throws Exception {
        // Empty on purpose.
    }

    @Override
    public void stop() throws Exception {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.shutdown();
        }
    }

    public Scheduler getScheduler() {
        Preconditions.checkNotNull(scheduler, "Scheduler not created yet.");
        return scheduler;
    }

    @Override
    public Scheduler get() {
        return getScheduler();
    }

    private Properties getProperties() {
        Properties props = new Properties();

        props.setProperty("org.quartz.jobStore.class", quartzConfig.getJobStore().getName());
        props.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfig.getThreads()));

        if (!quartzConfig.getJobStore().equals(RAMJobStore.class)) {
            props.setProperty("org.quartz.jobStore.driverDelegateClass", quartzConfig.getDbDelegate().getName());
            props.setProperty("org.quartz.jobStore.dataSource", "main");
            props.setProperty("org.quartz.dataSource.main.driver", dbConfig.getDriverClass());
            props.setProperty("org.quartz.dataSource.main.URL", dbConfig.getUrl());
            props.setProperty("org.quartz.dataSource.main.user", dbConfig.getUser());
            props.setProperty("org.quartz.dataSource.main.password", dbConfig.getPassword());
        }

        return props;
    }
}
