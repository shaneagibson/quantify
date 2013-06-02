package uk.co.epsilontechnologies.quantify.jmx;

public interface QuantifyMBean {

    public static final String MBEAN_NAME = "uk.co.epsilontechnologies.quantify.jmx:type=quantify";

    void quantify(
            final String className,
            final String methodName,
            final String threadName,
            final Long start,
            final Long end);

}
