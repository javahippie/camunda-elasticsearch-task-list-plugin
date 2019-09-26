package de.javahippie.camunda.listener.task;

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
    void processElasticSearchRequest(String taskId, Map<String, Object> variableMap) {
        super.update(taskId, variableMap);
    }

}
