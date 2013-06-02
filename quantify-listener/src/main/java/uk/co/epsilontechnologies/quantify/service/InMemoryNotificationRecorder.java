package uk.co.epsilontechnologies.quantify.service;

import org.springframework.stereotype.Component;
import uk.co.epsilontechnologies.quantify.model.MethodInvocation;
import uk.co.epsilontechnologies.quantify.persistence.MethodInvocationDataStore;

@Component
public class InMemoryNotificationRecorder implements NotificationRecorder {

    @Override
    public void record(final MethodInvocation methodInvocation) {
        MethodInvocationDataStore.getInstance().add(methodInvocation);
    }

}
