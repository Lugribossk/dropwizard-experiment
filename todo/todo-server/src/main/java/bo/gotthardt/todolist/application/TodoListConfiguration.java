package bo.gotthardt.todolist.application;

import bo.gotthardt.ebean.HasDatabaseConfiguration;
import bo.gotthardt.email.EmailServiceConfiguration;
import bo.gotthardt.email.HasEmailServiceConfiguration;
import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

/**
 * @author Bo Gotthardt
 */
@Getter
public class TodoListConfiguration extends Configuration implements HasDatabaseConfiguration, HasEmailServiceConfiguration, HasSendGridConfiguration {
    private DataSourceFactory database;
    private EmailServiceConfiguration email;
    private SendGridConfiguration sendGrid;
}
