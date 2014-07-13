package bo.gotthardt.schedule.quartz;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

/**
 * Borrowed from https://github.com/spinscale/dropwizard-jobs/blob/master/dropwizard-jobs-guice/src/main/java/de/spinscale/dropwizard/jobs/GuiceJobFactory.java
 */
class GuiceJobFactory implements JobFactory {
    private final Injector injector;

    @Inject
    public GuiceJobFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        Class<? extends Job> jobClass = jobDetail.getJobClass();
        return injector.getInstance(jobClass);
    }
}
