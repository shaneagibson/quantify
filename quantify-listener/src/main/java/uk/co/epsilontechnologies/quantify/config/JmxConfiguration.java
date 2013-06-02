package uk.co.epsilontechnologies.quantify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import uk.co.epsilontechnologies.quantify.jmx.QuantifyMBean;

@Configuration
@EnableMBeanExport
public class JmxConfiguration {

    private static final int DEFAULT_JMX_PORT = 1099;
    private static final String DEFAULT_JMX_HOST = "localhost";

    private static final String JMX_PORT_PROPERTY = "jmx.port";
    private static final String JMX_HOST_PROPERTY = "jmx.host";

    public static int resolveJmxPort() {
        return System.getProperty(JMX_PORT_PROPERTY) != null ? Integer.parseInt(System.getProperty(JMX_PORT_PROPERTY)) : DEFAULT_JMX_PORT;
    }

    public static String resolveJmxHost() {
        return System.getProperty(JMX_HOST_PROPERTY) != null ? System.getProperty(JMX_HOST_PROPERTY) : DEFAULT_JMX_HOST;
    }

    static {
        System.setProperty("java.rmi.server.hostname", resolveJmxHost());
        System.setProperty("com.sun.management.jmxremote.port", String.valueOf(resolveJmxPort()));
        System.setProperty("com.sun.management.jmxremote.ssl.need.client.auth", "false");
        System.setProperty("com.sun.management.jmxremote.ssl", "false");
        System.setProperty("com.sun.management.jmxremote.authenticate", "false");
    }

    @Bean
    public RmiRegistryFactoryBean rmiRegistry() {
        final RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
        rmiRegistryFactoryBean.setPort(resolveJmxPort());
        return rmiRegistryFactoryBean;
    }

    @Bean
    @DependsOn("rmiRegistry")
    public ConnectorServerFactoryBean connectorServerFactoryBean() throws Exception {
        final ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
        connectorServerFactoryBean.setObjectName("connector:name=rmi");
        connectorServerFactoryBean.setServiceUrl(String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", resolveJmxHost(), resolveJmxPort()));
        return connectorServerFactoryBean;
    }

}