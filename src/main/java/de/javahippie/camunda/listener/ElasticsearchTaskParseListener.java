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
    private final Client elasticsearchClient;

    public ElasticsearchTaskParseListener() throws UnknownHostException {
        this.elasticsearchClient = ElasticClientBuilder.create()
                .clusterName("docker-cluster")
                .domainName("localhost")
                .port(9200)
                .build();
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        LOGGER.info("Adding Task Listener to User Task:"
                + " activityId=" + activity.getId()
                + ", activityName='" + activity.getName() + "'"
                + ", scopeId=" + scope.getId()
                + ", scopeName=" + scope.getName());

        ActivityBehavior behavior = activity.getActivityBehavior();
        if (behavior instanceof UserTaskActivityBehavior) {
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) behavior).getTaskDefinition();
            taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new ElasticsearchTaskAssignListener(elasticsearchClient));
            taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new ElasticsearchTaskDeleteListener(elasticsearchClient));
            taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new ElasticsearchTaskCreateListener(elasticsearchClient));
            taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, new ElasticsearchTaskDeleteListener(elasticsearchClient));
        }
    }

}
