package de.javahippie.camunda.elasticsearchtasktester;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Fake Service delegate, used for all service tasks in the example application
 */
@Component("fakeDelegate")
public class FakeDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //Do nothing, it's fake.
    }

}
