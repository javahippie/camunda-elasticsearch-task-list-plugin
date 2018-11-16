package de.javahippie.camunda.listener;

import org.camunda.bpm.engine.delegate.TaskListener;
import org.elasticsearch.client.Client;

public abstract class AbstractElasticsearchTaskListener implements TaskListener {

    private final Client elasticSearchClient;

    public AbstractElasticsearchTaskListener(Client elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    protected Client getElasticSearchClient() {
        return this.elasticSearchClient;
    }
}
