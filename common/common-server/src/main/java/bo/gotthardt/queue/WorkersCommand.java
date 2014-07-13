package bo.gotthardt.queue;

import bo.gotthardt.schedule.HasScheduleConfigurations;
import bo.gotthardt.schedule.ScheduleConfiguration;
import bo.gotthardt.schedule.quartz.HasQuartzConfiguration;
import bo.gotthardt.schedule.quartz.Quartz;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Dropwizard command for running message queue workers and scheduled jobs.
 * Message queue workers can be configured with which worker classes to instantiate, and how many threads of each.
 * Scheduled jobs can be configured with which job classes to instantiate, and a cron expression for when they should run.
 *
 * Due to weirdness with the Dropwizard initialization order, a Guice Injector must be set after creation.
 * Make sure it has been set up for providing all the dependencies required for the workers and jobs being used.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class WorkersCommand<T extends Configuration & HasWorkerConfigurations & HasScheduleConfigurations & HasQuartzConfiguration> extends EnvironmentCommand<T> {
    @Setter
    private Injector injector;

    /**
     * Constructor.
     * @param application The application running this command.
     */
    public WorkersCommand(Application<T> application) {
        super(application, "workers", "Runs message queue workers and scheduled jobs.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        Preconditions.checkNotNull(injector, "Injector not set, perhaps the run() execution order changed?");

        if (!configuration.getWorkers().isEmpty()) {
            setupWorkers(configuration.getWorkers(), environment, injector);
        }

        if (!configuration.getSchedules().isEmpty()) {
            Quartz quartz = new Quartz(configuration.getQuartz(), configuration.getDatabase(), injector);
            environment.lifecycle().manage(quartz);

            setupSchedules(configuration.getSchedules(), quartz.getScheduler());
        }
    }

    private static void setupWorkers(List<WorkerConfiguration> workers, Environment environment, Injector injector) {
        workers.forEach(config -> {
            Class<? extends QueueWorker> workerClass = config.getWorker();
            ExecutorService executorService = environment.lifecycle()
                    .executorService(workerClass.getSimpleName() + "-%d")
                    .maxThreads(config.getThreads())
                    .build();

            for (int i = 0; i < config.getThreads(); i++) {
                QueueWorker<?> worker = injector.getInstance(workerClass);
                executorService.submit(worker);
            }
            log.info("Created {} thread{} for worker {}.", config.getThreads(), config.getThreads() > 1 ? "s" : "", workerClass.getSimpleName());
        });
    }

    private static void setupSchedules(List<ScheduleConfiguration> schedules, Scheduler scheduler) {
        Set<JobKey> configuredKeys = new HashSet<>();

        schedules.forEach(config -> {
            Class<? extends Job> jobClass = config.getJob();
            JobKey jobKey = new JobKey(config.getName(), "cron");
            TriggerKey triggerKey = new TriggerKey(config.getName(), "cron");
            configuredKeys.add(jobKey);

            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .storeDurably()
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron()))
                    .forJob(job)
                    .build();

            try {
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.rescheduleJob(triggerKey, trigger);
                } else {
                    scheduler.addJob(job, true);
                    scheduler.scheduleJob(trigger);
                }
                log.info("Scheduled job {} to run at '{}'", jobClass.getSimpleName(), config.getCron().getCronExpression());
            } catch (SchedulerException e) {
                log.warn("Unable to schedule job {}", jobClass.getSimpleName(), e);
            }
        });

        try {
            Set<JobKey> existingKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>jobGroupEquals("cron"));
            Set<JobKey> removedKeys = Sets.difference(existingKeys, configuredKeys);
            scheduler.deleteJobs(Lists.newArrayList(removedKeys));

            log.info("Deleted jobs that are no longer configured: {}", removedKeys.stream().map(Key::getName));
        } catch (SchedulerException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }
}
