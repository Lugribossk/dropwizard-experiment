package bo.gotthardt.schedule;

import lombok.Getter;
import org.quartz.CronExpression;
import org.quartz.Job;

@Getter
public class ScheduleConfiguration {
    private String name;
    private Class<? extends Job> job;
    private CronExpression cron;
}
