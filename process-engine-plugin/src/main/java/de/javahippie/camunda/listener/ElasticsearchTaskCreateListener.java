package de.javahippie.camunda.listener;

import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.Index;

/**
 * Updates an existing document with the taskId as ID every time when a task is created.
 */
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

        IndexRequest request = createIndexRequestFromDelegateTask(task);
        getElasticSearchClient().index(request);
    }

    private IndexRequest createIndexRequestFromDelegateTask(DelegateTask delegateTask) {
        Map<String, Object> variableMap = delegateTask.getVariables();
        variableMap.put("taskName", delegateTask.getName());
        variableMap.put("assignee", delegateTask.getAssignee());
        return getElasticSearchClient().prepareIndex("camunda-ex", "_doc", delegateTask.getId())
                .setSource(variableMap)
                .request();
    }

}
