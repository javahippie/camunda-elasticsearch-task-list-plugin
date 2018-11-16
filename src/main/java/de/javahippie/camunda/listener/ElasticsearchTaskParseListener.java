package de.javahippie.camunda.listener;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import de.javahippie.camunda.elasticsearch.ElasticClientBuilder;
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

public class ElasticsearchTaskParseListener extends AbstractBpmnParseListener implements BpmnParseListener {

    private final Logger LOGGER = Logger.getLogger(ElasticsearchTaskParseListener.class.getName());
    private ElasticClientBuilder elasticsearchClientBuilder;

    public void setElasticsearchClientBuilder(ElasticClientBuilder elasticsearchClientBuilder) {
        this.elasticsearchClientBuilder = elasticsearchClientBuilder;
    }

    public ElasticClientBuilder getElasticsearchClientBuilder() {
        return elasticsearchClientBuilder;
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        try {
            Client elasticSearchClient = elasticsearchClientBuilder.build();
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
