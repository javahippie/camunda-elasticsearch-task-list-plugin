package de.javahippie.camunda.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Simple builder to create an Elasticsearch Client
 */
public class ElasticClientBuilder {

    public static final String PROPERTY_CLUSTER_NAME = "cluster.name";

    private String clusterName;
    private String domainName;
    private int port;

    private ElasticClientBuilder() {
        //We don't want to instantiate this by hand
    }

    /**
     * Creates an instance of this builder
     *
     * @return an instance of ElasticClientBuilder
     */
    public static ElasticClientBuilder create() {
        return new ElasticClientBuilder();
    }

    /**
     * Set the clustername, matching the 'cluster.name' variable of your Elasticsearch instance
     *
     * @param clusterName the cluster name of the Elasticsearch instance
     * @return a reference to this builder
     */
    public ElasticClientBuilder clusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    /**
     * Set the domain name of the Elasticsearch instance we want to connect to
     *
     * @param domainName the domain name of the Elasticsearch instance
     * @return a reference to this builder
     */
    public ElasticClientBuilder domainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    /**
     * Set the port of your Elasticsearch instance (default: 9200)
     *
     * @param port the port of the Elasticsearch instance
     * @return a reference to this builder
     */
    public ElasticClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * Create an instance of the Elasticsearch client, based on the previously defined configurations
     *
     * @return an instantiated elasticsearch client
     * @throws UnknownHostException if the instance cannot be accessed via the given domain name
     */
    public Client build() throws UnknownHostException {
        Settings settings = Settings.builder().put(PROPERTY_CLUSTER_NAME, this.clusterName).build();
        TransportAddress address = new TransportAddress(InetAddress.getByName(this.domainName), this.port);
        return new PreBuiltTransportClient(settings).addTransportAddress(address);
    }

}
