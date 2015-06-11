package bo.gotthardt.schedule.quartz;

import org.glassfish.hk2.api.ServiceLocator;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

/**
 * A Quartz JobFactory that uses HK2 dependency injection to create Job instances.
 */
public class HK2JobFactory implements JobFactory {
    private final ServiceLocator locator;

    @Inject
    public HK2JobFactory(ServiceLocator locator) {
        this.locator = locator;
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        Class<? extends Job> jobClass = jobDetail.getJobClass();
        return locator.getService(jobClass);
    }
}
