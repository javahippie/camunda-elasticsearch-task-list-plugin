package de.javahippie.camunda.listener.task;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.elasticsearch.client.Client;

import java.util.logging.Logger;

/**
 * Updates an existing document with the taskId as ID every time when a task is deleted or completed.
 */
public class ElasticsearchTaskDeleteListener extends AbstractElasticsearchTaskListener {

    public ElasticsearchTaskDeleteListener(Client elasticSearchClient) {
        super(elasticSearchClient);
    }

    @Override
    void processElasticSearchRequest(DelegateTask task) {
        super.delete(task.getId());
    }
}
