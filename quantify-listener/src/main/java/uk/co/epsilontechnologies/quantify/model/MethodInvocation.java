package uk.co.epsilontechnologies.quantify.model;

public class MethodInvocation extends AbstractModelObject {

    private final String className;
    private final String methodName;
    private final String threadName;
    private final Long startNanos;
    private final Long endNanos;

    public MethodInvocation(
            final String className,
            final String methodName,
            final String threadName,
            final Long startNanos,
            final Long endNanos) {
        this.className = className;
        this.methodName = methodName;
        this.threadName = threadName;
        this.startNanos = startNanos;
        this.endNanos = endNanos;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Long getStartNanos() {
        return startNanos;
    }

    public String getThreadName() {
        return threadName;
    }

    public Long getEndNanos() {
        return endNanos;
    }

}
