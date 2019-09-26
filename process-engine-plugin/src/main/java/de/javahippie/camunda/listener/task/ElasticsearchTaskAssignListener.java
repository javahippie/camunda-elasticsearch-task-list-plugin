package de.javahippie.camunda.listener.task;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.elasticsearch.client.Client;

import java.util.Map;

/**
 * Updates an existing document with the taskId as ID every time when a task is assigned.
 * Also fires, when a task is created and immediately assigned!
 */
public class ElasticsearchTaskAssignListener extends AbstractElasticsearchTaskListener {

    public ElasticsearchTaskAssignListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    void processElasticSearchRequest(DelegateTask task) {
        Map<String, Object> variableMap = task.getVariables();
        variableMap.put(TASK_NAME_ATTRIBUTE, task.getName());
        variableMap.put(ASSIGNEE_ATTRIBUTE, task.getAssignee());

        super.update(task.getId(), variableMap);
    }

}
