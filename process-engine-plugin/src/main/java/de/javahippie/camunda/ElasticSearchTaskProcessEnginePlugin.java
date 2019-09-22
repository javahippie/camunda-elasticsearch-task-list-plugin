package de.javahippie.camunda;

import de.javahippie.camunda.listener.ElasticsearchTaskParseListener;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Process Engine Plugin which communicates state changes regarding User Tasks to an Elasticsearch instance
 */
public class ElasticSearchTaskProcessEnginePlugin implements ProcessEnginePlugin {

    private String domainName;
    private int port;
    private String clusterName;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> postParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (null == postParseListeners) {
            postParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(postParseListeners);
        }
        postParseListeners.add(new ElasticsearchTaskParseListener(domainName, port, clusterName));
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // We don't do anything in the postInit
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        // We don't do anything in the preInit
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
