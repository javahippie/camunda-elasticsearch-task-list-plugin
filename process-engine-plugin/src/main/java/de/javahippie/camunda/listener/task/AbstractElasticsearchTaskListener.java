package de.javahippie.camunda.listener.task;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cfg.TransactionListener;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Abstract parent class for Task listeners that interact with an Elasticsearch instance
 */
public abstract class AbstractElasticsearchTaskListener implements TaskListener {

    public static final String TASK_NAME_ATTRIBUTE = "taskName";
    public static final String ASSIGNEE_ATTRIBUTE = "assignee";

    private static final String INDEX_ID = "camunda-ex";
    private static final String INDEX_TYPE_DOC = "_doc";

    private final Logger LOGGER = Logger.getLogger(AbstractElasticsearchTaskListener.class.getName());

    private final Client elasticSearchClient;

    public AbstractElasticsearchTaskListener(Client elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    /**
     * Custom logic for the separate listeners goes here.
     *
     * @param task The task, which is handed to this TaskListener
     */
    abstract void processElasticSearchRequest(DelegateTask task);

    @Override
    public void notify(DelegateTask delegateTask) {
        LOGGER.info("Event '" + delegateTask.getEventName() + "' received by " + this.getClass().getName() + " for Task:"
                + " activityId=" + delegateTask.getTaskDefinitionKey()
                + ", name='" + delegateTask.getName() + "'"
                + ", taskId=" + delegateTask.getId()
                + ", assignee='" + delegateTask.getAssignee() + "'"
                + ", candidateGroups='" + delegateTask.getCandidates() + "'");

        registerTransactionListener(commandContext -> processElasticSearchRequest(delegateTask));
    }

    protected void index(String taskId, Map<String, Object> variables) {
        IndexRequest request = this.elasticSearchClient.prepareIndex(INDEX_ID, INDEX_TYPE_DOC, taskId)
                .setSource(variables)
                .request();
        this.elasticSearchClient.index(request);
    }

    protected void update(String taskId, Map<String, Object> variables) {
        UpdateRequest request = this.elasticSearchClient.prepareUpdate(INDEX_ID, INDEX_TYPE_DOC, taskId)
                .setDoc(variables)
                .request();
        this.elasticSearchClient.update(request);
    }

    protected void delete(String taskId) {
        DeleteRequest request = this.elasticSearchClient.prepareDelete(INDEX_ID, INDEX_TYPE_DOC, taskId).request();
        this.elasticSearchClient.delete(request);
    }

    /**
     * Abstraction for the static call to the command context. The txListener can be passed as a lambda to keep the single listeners clean.
     *
     * @param txListener The transaction listener to be attached to the current transaction
     */
    private void registerTransactionListener(TransactionListener txListener) {
        Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.COMMITTED, txListener);
    }
}
