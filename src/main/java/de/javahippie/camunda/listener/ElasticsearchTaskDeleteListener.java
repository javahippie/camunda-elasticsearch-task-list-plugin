package de.javahippie.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.elasticsearch.client.Client;

import java.util.logging.Logger;

public class ElasticsearchTaskDeleteListener extends AbstractElasticsearchTaskListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchTaskDeleteListener.class.getName());

    public ElasticsearchTaskDeleteListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    public void notify(DelegateTask task) {
        LOGGER.info("Event '" + task.getEventName() + "' received by ElasticsearchTaskDeleteListener for Task:"
                + " activityId=" + task.getTaskDefinitionKey()
                + ", name='" + task.getName() + "'"
                + ", taskId=" + task.getId()
                + ", assignee='" + task.getAssignee() + "'"
                + ", candidateGroups='" + task.getCandidates() + "'");
    }

}
