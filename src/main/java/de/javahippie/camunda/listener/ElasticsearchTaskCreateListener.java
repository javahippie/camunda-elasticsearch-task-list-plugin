package de.javahippie.camunda.listener;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.elasticsearch.client.Client;

public class ElasticsearchTaskCreateListener extends AbstractElasticsearchTaskListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchTaskCreateListener.class.getName());

    public ElasticsearchTaskCreateListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    public void notify(DelegateTask task) {
        LOGGER.info("Event '" + task.getEventName() + "' received by ElasticsearchTaskCreateListener for Task:"
                + " activityId=" + task.getTaskDefinitionKey()
                + ", name='" + task.getName() + "'"
                + ", taskId=" + task.getId()
                + ", assignee='" + task.getAssignee() + "'"
                + ", candidateGroups='" + task.getCandidates() + "'");
    }

}
