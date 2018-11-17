package de.javahippie.camunda.listener;

import de.javahippie.camunda.elasticsearch.ElasticClientConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Process Engine Plugin which communicates state changes regarding User Tasks to an Elasticsearh instance
 */
public class ElasticSearchTaskProcessEnginePlugin implements ProcessEnginePlugin {

    private ElasticClientConfig config;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> postParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (postParseListeners == null) {
            postParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(postParseListeners);
        }
        ElasticsearchTaskParseListener listener = new ElasticsearchTaskParseListener();

        listener.setElasticClientConfig(config);
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

    /**
     * Set the configuration of the Elasticsearch instance. Should be configured from camunda.cfg.xml.
     * Needs to be called before the preInit() call!
     * @param config Basic configuration for the Elastic Search client
     */
    public void setConfig(ElasticClientConfig config) {
        this.config = config;
    }
}
