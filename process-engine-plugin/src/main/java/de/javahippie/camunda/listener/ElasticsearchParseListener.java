package de.javahippie.camunda.listener;

import de.javahippie.camunda.listener.task.ElasticsearchTaskAssignListener;
import de.javahippie.camunda.listener.task.ElasticsearchTaskDeleteListener;
import de.javahippie.camunda.listener.task.ElasticsearchTaskIndexListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.elasticsearch.client.Client;

import java.util.logging.Logger;

/**
 * This parse listener attaches the TaskListeners that interact with ElasticSearch on every User Task.
 */
public class ElasticsearchParseListener extends AbstractBpmnParseListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchParseListener.class.getName());

    private final Client elasticSearchClient;

    public ElasticsearchParseListener(Client elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {

        LOGGER.info("Adding Task Listener to User Task:"
                + " activityId=" + activity.getId()
                + ", activityName='" + activity.getName() + "'"
                + ", scopeId=" + scope.getId()
                + ", scopeName=" + scope.getName());

        ActivityBehavior behavior = activity.getActivityBehavior(); // Will always be a UserTaskActivityBehavior
        TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new ElasticsearchTaskAssignListener(this.elasticSearchClient));
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new ElasticsearchTaskDeleteListener(this.elasticSearchClient));
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new ElasticsearchTaskIndexListener(this.elasticSearchClient));
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, new ElasticsearchTaskDeleteListener(this.elasticSearchClient));

    }

}
