package de.javahippie.camunda.elasticsearch;

import org.elasticsearch.client.Client;

import java.net.UnknownHostException;

public interface ElasticClientConfig {
    void setClusterName(String clusterName);

    void setDomainName(String domainName);

    void setPort(int port);

    Client build() throws UnknownHostException;
}
