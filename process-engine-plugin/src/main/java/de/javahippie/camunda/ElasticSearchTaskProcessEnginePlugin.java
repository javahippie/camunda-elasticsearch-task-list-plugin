package de.javahippie.camunda;

import de.javahippie.camunda.elasticsearch.ElasticClientConfig;
import de.javahippie.camunda.listener.ElasticsearchParseListener;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Process Engine Plugin which communicates state changes regarding User Tasks to an Elasticsearch instance
 */
public class ElasticSearchTaskProcessEnginePlugin implements ProcessEnginePlugin {

    private final Logger LOGGER = Logger.getLogger(ElasticSearchTaskProcessEnginePlugin.class.getName());

    private String domainName;
    private int port;
    private String clusterName;

    private ElasticClientConfig elasticClientConfig;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> postParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (null == postParseListeners) {
            postParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(postParseListeners);
        }
        try {
            this.elasticClientConfig = loadElasticSearchClientConfig(domainName, port, clusterName);
            postParseListeners.add(new ElasticsearchParseListener(elasticClientConfig.build()));
            LOGGER.log(Level.INFO, "Instantiated Elasticsearch client");
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Could not establish a connection the the Elasticsearch instance - unknown Host", e);
        }
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // We don't do anything in the postInit
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        // We don't do anything in the preInit
    }

    private ElasticClientConfig loadElasticSearchClientConfig(String domainName, int port, String clusterName) {
        ElasticClientConfig elasticClientConfig = loadElasticClientConfigFromClassPath();
        elasticClientConfig.setDomainName(domainName);
        elasticClientConfig.setPort(port);
        elasticClientConfig.setClusterName(clusterName);
        return elasticClientConfig;
    }

    private ElasticClientConfig loadElasticClientConfigFromClassPath() {
        ServiceLoader<ElasticClientConfig> loader = ServiceLoader.load(ElasticClientConfig.class);
        List<ElasticClientConfig> configurations = new ArrayList<>();
        loader.forEach(configurations::add);
        return configurations.get(0);
    }

    ElasticClientConfig getElasticClientConfig() {
        return elasticClientConfig;
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
