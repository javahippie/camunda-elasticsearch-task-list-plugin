package de.javahippie.camunda.listener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

public class ElasticSearchTaskProcessEnginePlugin implements ProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> postParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (postParseListeners == null) {
            postParseListeners = new ArrayList<BpmnParseListener>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(postParseListeners);
        }
        //FIXME: Swallowing the exception is not ideal
        try {
            postParseListeners.add(new ElasticsearchTaskParseListener());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        //Nothing to do
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        //Nothing to do
    }

}
