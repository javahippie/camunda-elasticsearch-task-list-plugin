package de.javahippie.camunda;

import de.javahippie.camunda.elasticsearch.MockedElasticClientConfig;
import de.javahippie.camunda.listener.ElasticsearchTaskParseListener;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.init;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class InMemoryH2Test {

    @ClassRule
    @Rule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    private static final String PROCESS_DEFINITION_KEY = "elasticsearch-task-plugin";

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    private MockedElasticClientConfig builder;

    @Before
    public void setup() {

        Optional<ProcessEnginePlugin> elasticSearchplugin = rule.getProcessEngineConfiguration()
                .getProcessEnginePlugins()
                .stream()
                .filter(plugin -> plugin instanceof ElasticSearchTaskProcessEnginePlugin).findFirst();


        ElasticSearchTaskProcessEnginePlugin plugin = (ElasticSearchTaskProcessEnginePlugin) elasticSearchplugin.get();
        this.builder = (MockedElasticClientConfig) plugin.getElasticClientConfig();

        init(rule.getProcessEngine());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testHappyPath() {
        ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);

        assertThat(processInstance).task("Task_DoSomething");


        Mockito.verify(builder.client.irb, Mockito.times(1)).setSource(explodedMap("taskName", "Do something", "assignee", "demo"));
        Mockito.verify(builder.client.irb, Mockito.times(1)).request();
        Mockito.verify(builder.client.urb, Mockito.times(1)).setDoc(explodedMap("taskName", "Do something", "assignee", "demo"));
        Mockito.verify(builder.client.urb, Mockito.times(1)).request();

        complete(task());

        Mockito.verify(builder.client.drb, Mockito.times(1)).request();

        assertThat(processInstance).task("Task_DoSomethingElse");

        Mockito.verify(builder.client.irb, Mockito.times(1)).setSource(explodedMap("taskName", "Do something else", "assignee", "demo"));
        Mockito.verify(builder.client.irb, Mockito.times(2)).request();
        Mockito.verify(builder.client.urb, Mockito.times(1)).setDoc(explodedMap("taskName", "Do something else", "assignee", "demo"));
        Mockito.verify(builder.client.urb, Mockito.times(2)).request();

        taskService().setAssignee(task().getId(), "john");
        assertThat(task()).isAssignedTo("john");

        Mockito.verify(builder.client.urb, Mockito.times(1)).setDoc(explodedMap("taskName", "Do something else", "assignee", "john"));
        Mockito.verify(builder.client.urb, Mockito.times(3)).request();

        processEngine().getRuntimeService().deleteProcessInstance(processInstance.getId(), "deleted for test reasons");

        Mockito.verify(builder.client.drb, Mockito.times(2)).request();

        assertThat(processInstance).isEnded();
    }

    private Map<String, Object> explodedMap(String... content) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < content.length; i = i + 2) {
            map.put(content[i], content[i + 1]);
        }
        return map;
    }
}
