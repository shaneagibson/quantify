package uk.co.epsilontechnologies.quantify;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import uk.co.epsilontechnologies.quantify.config.ApplicationConfiguration;
import uk.co.epsilontechnologies.quantify.config.WebConfiguration;

import java.io.File;

public class Runner {

    private static final String CONTEXT_PATH = "/quantify";

    private static final int DEFAULT_WEBAPP_PORT = 8091;

    private static final String WEBAPP_PORT_PROPERTY = "webapp.port";

    public static void main(final String... args) throws Exception {

        final int webappPort = System.getProperty(WEBAPP_PORT_PROPERTY) != null ? Integer.parseInt(System.getProperty(WEBAPP_PORT_PROPERTY)) : DEFAULT_WEBAPP_PORT;

        final String baseDirectory = new File("src/main/webapp/").getAbsolutePath();
        final String tmpDirectory = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(webappPort);
        tomcat.setBaseDir(tmpDirectory);

        final Context context = tomcat.addWebapp(CONTEXT_PATH, baseDirectory);

        context.addParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.addParameter("contextConfigLocation", ApplicationConfiguration.class.getName());

        context.addApplicationListener(ContextLoaderListener.class.getName());

        final Wrapper springServlet = context.createWrapper();
        springServlet.setName("spring-servlet");
        springServlet.setServletClass(DispatcherServlet.class.getName());
        springServlet.addInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        springServlet.addInitParameter("contextConfigLocation", WebConfiguration.class.getName());
        springServlet.setLoadOnStartup(1);
        context.addChild(springServlet);
        context.addServletMapping("/api/*", "spring-servlet");

        tomcat.start();
        tomcat.getServer().await();
    }

}