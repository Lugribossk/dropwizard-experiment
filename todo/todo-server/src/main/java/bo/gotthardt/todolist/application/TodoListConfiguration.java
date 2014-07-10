package bo.gotthardt.todolist.application;

import bo.gotthardt.ebean.HasDatabaseConfiguration;
import bo.gotthardt.email.EmailServiceConfiguration;
import bo.gotthardt.email.HasEmailServiceConfiguration;
import bo.gotthardt.email.sendgrid.HasSendGridConfiguration;
import bo.gotthardt.email.sendgrid.SendGridConfiguration;
import bo.gotthardt.queue.HasWorkerConfigurations;
import bo.gotthardt.queue.WorkerConfiguration;
import bo.gotthardt.queue.rabbitmq.HasRabbitMQConfiguration;
import bo.gotthardt.queue.rabbitmq.RabbitMQConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bo Gotthardt
 */
@Getter
public class TodoListConfiguration extends Configuration implements HasDatabaseConfiguration, HasEmailServiceConfiguration, HasSendGridConfiguration, HasRabbitMQConfiguration, HasWorkerConfigurations {
    private DataSourceFactory database;
    private EmailServiceConfiguration email = new EmailServiceConfiguration();
    private SendGridConfiguration sendGrid;
    private RabbitMQConfiguration rabbitMq = new RabbitMQConfiguration();
    private List<WorkerConfiguration> workers = new ArrayList<>();
}
