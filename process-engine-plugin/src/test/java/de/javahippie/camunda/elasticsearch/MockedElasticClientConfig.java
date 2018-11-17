package de.javahippie.camunda.elasticsearch;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.mockito.Mockito;

import java.util.Map;

/**
 * Mocked instance of the ElasticClientLiveConfig which can be injected into
 * the Process Engine plugin in the camunda.cfg.xml
 */
public class MockedElasticClientConfig implements ElasticClientConfig {

    public final MockedElasticClient client = new MockedElasticClient();

    @Override
    public void setClusterName(String clusterName) {
        //do nothing
    }

    @Override
    public void setDomainName(String domainName) {
        //do nothing
    }

    @Override
    public void setPort(int port) {
        //do nothing
    }

    @Override
    public Client build() {
        return client;
    }

    private UpdateRequestBuilder mockUpdateRequestBuilder() {
        UpdateRequestBuilder builder = Mockito.mock(UpdateRequestBuilder.class);
        Mockito.when(builder.setDoc(Mockito.any(Map.class))).thenReturn(builder);
        Mockito.when(builder.request()).thenReturn(Mockito.mock(UpdateRequest.class));
        return builder;
    }

    private IndexRequestBuilder mockIndexRequestBuilder() {
        IndexRequestBuilder builder = Mockito.mock(IndexRequestBuilder.class);
        Mockito.when(builder.setSource(Mockito.any(Map.class))).thenReturn(builder);
        Mockito.when(builder.request()).thenReturn(Mockito.mock(IndexRequest.class));
        return builder;
    }

    private DeleteRequestBuilder mockDeleteRequestBuilder() {
        DeleteRequestBuilder builder = Mockito.mock(DeleteRequestBuilder.class);
        Mockito.when(builder.request()).thenReturn(Mockito.mock(DeleteRequest.class));
        return builder;
    }

    public class MockedElasticClient extends PreBuiltTransportClient {

        public MockedElasticClient() {
            super(Settings.EMPTY);
        }

        public IndexRequestBuilder irb = mockIndexRequestBuilder();
        public UpdateRequestBuilder urb = mockUpdateRequestBuilder();
        public DeleteRequestBuilder drb = mockDeleteRequestBuilder();

        @Override
        protected <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> void doExecute(Action<Request, Response, RequestBuilder> action, Request request, ActionListener<Response> listener) {

        }

        @Override
        public void close() {

        }

        @Override
        public IndexRequestBuilder prepareIndex(String index, String type, String id) {
            return irb;
        }

        @Override
        public UpdateRequestBuilder prepareUpdate(String index, String type, String id) {
            return urb;
        }

        @Override
        public DeleteRequestBuilder prepareDelete(String index, String type, String id) {
            return drb;
        }
    }


}

