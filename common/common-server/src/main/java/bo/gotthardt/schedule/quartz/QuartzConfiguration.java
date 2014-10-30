package bo.gotthardt.schedule.quartz;

import lombok.Getter;
import org.quartz.impl.jdbcjobstore.DriverDelegate;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.simpl.RAMJobStore;
import org.quartz.spi.JobStore;

@Getter
public class QuartzConfiguration {
    private int threads = 10;
    private Class<? extends JobStore> jobStore = RAMJobStore.class;
    private Class<? extends DriverDelegate> dbDelegate = StdJDBCDelegate.class;
}
