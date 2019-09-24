package de.javahippie.camunda.elasticsearchtasktester;

import de.javahippie.camunda.ElasticSearchTaskProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class AppConfig {

    @Bean
    @Order(Ordering.DEFAULT_ORDER + 1)
    public static ProcessEnginePlugin registerProcessEnginePlugin() {
        ElasticSearchTaskProcessEnginePlugin elasticSearchPlugin = new ElasticSearchTaskProcessEnginePlugin();
        elasticSearchPlugin.setClusterName("docker-cluster");
        elasticSearchPlugin.setDomainName("localhost");
        elasticSearchPlugin.setPort(9300);
        return elasticSearchPlugin;
    }
}
