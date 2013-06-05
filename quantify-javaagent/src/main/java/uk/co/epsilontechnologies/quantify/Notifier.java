package uk.co.epsilontechnologies.quantify;

public interface Notifier {

    void notify(
            final String uuid,
            final String className,
            final String methodName,
            final String threadName,
            final long start,
            final long end) throws Exception;

}
