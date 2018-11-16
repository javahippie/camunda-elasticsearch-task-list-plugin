package de.javahippie.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Updates an existing document with the taskId as ID every time when a task is assigned.
 * Also fires, when a task is created and immediately assigned!
 */
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

        UpdateRequest updateRequest = createUpdateRequestFromDelegateTask(task);
        getElasticSearchClient().update(updateRequest);
    }

    private UpdateRequest createUpdateRequestFromDelegateTask(DelegateTask delegateTask) {
        Map<String, Object> variableMap = delegateTask.getVariables();
        variableMap.put("taskName", delegateTask.getName());
        variableMap.put("assignee", delegateTask.getAssignee());
        return getElasticSearchClient().prepareUpdate("camunda-ex", "_doc", delegateTask.getId())
                .setDoc(variableMap)
                .request();
    }
}
