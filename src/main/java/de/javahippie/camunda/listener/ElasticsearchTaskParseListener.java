package de.javahippie.camunda.listener;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import de.javahippie.camunda.elasticsearch.ElasticClientConfig;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.elasticsearch.client.Client;

/**
 * This parse listener attaches the TaskListeners that interact with ElasticSearch on every User Task.
 */
public class ElasticsearchTaskParseListener extends AbstractBpmnParseListener implements BpmnParseListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchTaskParseListener.class.getName());
    private ElasticClientConfig elasticClientConfig;

    /**
     * Set the simple configuration properties for the Elastic Search client. Needs to be set before parseUserTask()
     * @param elasticClientConfig The basic configuration data
     */
    public void setElasticClientConfig(ElasticClientConfig elasticClientConfig) {
        this.elasticClientConfig = elasticClientConfig;
    }

    public ElasticClientConfig getElasticClientConfig() {
        return elasticClientConfig;
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        try {
            Client elasticSearchClient = elasticClientConfig.build();
            LOGGER.info("Adding Task Listener to User Task:"
                    + " activityId=" + activity.getId()
                    + ", activityName='" + activity.getName() + "'"
                    + ", scopeId=" + scope.getId()
                    + ", scopeName=" + scope.getName());

            ActivityBehavior behavior = activity.getActivityBehavior();
            if (behavior instanceof UserTaskActivityBehavior) {
                TaskDefinition taskDefinition = ((UserTaskActivityBehavior) behavior).getTaskDefinition();
                taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new ElasticsearchTaskAssignListener(elasticSearchClient));
                taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new ElasticsearchTaskDeleteListener(elasticSearchClient));
                taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new ElasticsearchTaskCreateListener(elasticSearchClient));
                taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, new ElasticsearchTaskDeleteListener(elasticSearchClient));
            }
        } catch (UnknownHostException e) {
            //FIXME: uncool
            throw new RuntimeException(e);
        }


    }

}
