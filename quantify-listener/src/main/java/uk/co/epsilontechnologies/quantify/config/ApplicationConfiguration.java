package uk.co.epsilontechnologies.quantify.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@Import(
    value= {
        CacheConfiguration.class,
        JmxConfiguration.class
    })
@ComponentScan(
    basePackages = {
        "uk.co.epsilontechnologies.quantify.service",
        "uk.co.epsilontechnologies.quantify.jmx"
    })
public class ApplicationConfiguration {

}