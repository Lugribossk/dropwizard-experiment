package bo.gotthardt.schedule.quartz;

import bo.gotthardt.ebean.HasDatabaseConfiguration;

public interface HasQuartzConfiguration extends HasDatabaseConfiguration {
    QuartzConfiguration getQuartz();
}
