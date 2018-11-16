package de.javahippie.camunda.listener;

import org.camunda.bpm.engine.delegate.TaskListener;
import org.elasticsearch.client.Client;

/**
 * Abstract parent class for Task listeners that interact with an Elasticsearch instance
 */
public abstract class AbstractElasticsearchTaskListener implements TaskListener {

    private final Client elasticSearchClient;

    public AbstractElasticsearchTaskListener(Client elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    /**
     * Retrieve the Client for subclasses with this getter
     * @return The client which was provided in the constructor
     */
    protected Client getElasticSearchClient() {
        return this.elasticSearchClient;
    }

}
