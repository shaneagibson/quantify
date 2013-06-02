package uk.co.epsilontechnologies.quantify.service;

import uk.co.epsilontechnologies.quantify.model.MethodInvocation;

public interface NotificationRecorder {

    void record(final MethodInvocation methodInvocation);

}
