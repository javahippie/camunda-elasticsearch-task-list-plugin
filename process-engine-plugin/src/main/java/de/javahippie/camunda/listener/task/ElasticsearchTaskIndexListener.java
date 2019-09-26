package de.javahippie.camunda.listener.task;

import org.elasticsearch.client.Client;

import java.util.Map;

/**
 * Updates an existing document with the taskId as ID every time when a task is created.
 */
public class ElasticsearchTaskIndexListener extends AbstractElasticsearchTaskListener {

    public ElasticsearchTaskIndexListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    void processElasticSearchRequest(String taskId, Map<String, Object> variableMap) {
        super.index(taskId, variableMap);
    }
}
