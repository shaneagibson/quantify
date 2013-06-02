package uk.co.epsilontechnologies.quantify;

import uk.co.epsilontechnologies.quantify.jmx.QuantifyMBean;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JmxNotifier implements Notifier {

    private static final String JMX_HOST_PROPERTY = "jmx.host";
    private static final String JMX_PORT_PROPERTY = "jmx.port";

    private static final String DEFAULT_JMX_HOST = "localhost";
    private static final int DEFAULT_JMX_PORT = 1099;

    public static JmxNotifier instance = new JmxNotifier();

    private final QuantifyMBean quantifyMBean;
    private final ExecutorService executorService;

    public JmxNotifier() {
        try {
            final String host = System.getProperty(JMX_HOST_PROPERTY) != null ? System.getProperty(JMX_HOST_PROPERTY) : DEFAULT_JMX_HOST;
            final int port = System.getProperty(JMX_PORT_PROPERTY) != null ? Integer.parseInt(System.getProperty(JMX_PORT_PROPERTY)) : DEFAULT_JMX_PORT;
            final String url = String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", host, port);
            final JMXServiceURL jmxServiceUrl = new JMXServiceURL(url);
            final JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceUrl);
            final MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
            final ObjectName mBeanName = new ObjectName(QuantifyMBean.MBEAN_NAME);
            this.quantifyMBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanName, QuantifyMBean.class);
            this.executorService = Executors.newCachedThreadPool();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notify(
            final String className,
            final String methodName,
            final String threadName,
            final long start,
            final long end) throws Exception {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
            quantifyMBean.quantify(className, methodName, threadName, start, end);
            }
        });
    }

}