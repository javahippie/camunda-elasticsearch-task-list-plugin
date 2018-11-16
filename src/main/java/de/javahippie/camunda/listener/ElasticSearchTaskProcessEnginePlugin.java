package de.javahippie.camunda.listener;

import de.javahippie.camunda.elasticsearch.ElasticClientBuilder;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchTaskProcessEnginePlugin implements ProcessEnginePlugin {

    private ElasticClientBuilder builder;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> postParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (postParseListeners == null) {
            postParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(postParseListeners);
        }
        ElasticsearchTaskParseListener listener = new ElasticsearchTaskParseListener();
        listener.setElasticsearchClientBuilder(builder
                .clusterName("docker-cluster")
                .domainName("localhost")
                .port(9300));
        postParseListeners.add(listener);
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        //Nothing to do
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        //Nothing to do
    }

    public void setBuilder(ElasticClientBuilder builder) {
        this.builder = builder;
    }
}
