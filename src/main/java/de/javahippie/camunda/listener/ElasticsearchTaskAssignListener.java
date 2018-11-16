package de.javahippie.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.elasticsearch.client.Client;

import java.net.UnknownHostException;
import java.util.logging.Logger;

public class ElasticsearchTaskAssignListener extends AbstractElasticsearchTaskListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchTaskAssignListener.class.getName());

    public ElasticsearchTaskAssignListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    public void notify(DelegateTask task) {
        LOGGER.info("Event '" + task.getEventName() + "' received by ElasticsearchTaskAssignListener for Task:"
                + " activityId=" + task.getTaskDefinitionKey()
                + ", name='" + task.getName() + "'"
                + ", taskId=" + task.getId()
                + ", assignee='" + task.getAssignee() + "'"
                + ", candidateGroups='" + task.getCandidates() + "'");
    }

}
