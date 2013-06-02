package uk.co.epsilontechnologies.quantify.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import uk.co.epsilontechnologies.quantify.model.MethodInvocation;
import uk.co.epsilontechnologies.quantify.service.NotificationRecorder;

@Component
@ManagedResource(objectName = QuantifyMBean.MBEAN_NAME)
public class Quantify implements QuantifyMBean {

    private final NotificationRecorder notificationRecorder;

    @Autowired
    public Quantify(final NotificationRecorder notificationRecorder) {
        this.notificationRecorder = notificationRecorder;
    }

    @Override
    @ManagedOperation
    public void quantify(
            final String className,
            final String methodName,
            final String threadName,
            final Long start,
            final Long end) {
        this.notificationRecorder.record(
                new MethodInvocation(
                        className,
                        methodName,
                        threadName,
                        start,
                        end));
    }

}