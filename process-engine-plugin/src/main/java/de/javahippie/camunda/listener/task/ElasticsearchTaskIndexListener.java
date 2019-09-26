package de.javahippie.camunda.listener.task;

import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.elasticsearch.client.Client;

/**
 * Updates an existing document with the taskId as ID every time when a task is created.
 */
public class ElasticsearchTaskIndexListener extends AbstractElasticsearchTaskListener {

    public ElasticsearchTaskIndexListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    void processElasticSearchRequest(DelegateTask task) {
        Map<String, Object> variableMap = task.getVariables();
        variableMap.put(TASK_NAME_ATTRIBUTE, task.getName());
        variableMap.put(ASSIGNEE_ATTRIBUTE, task.getAssignee());

        super.index(task.getId(), variableMap);
    }
}
