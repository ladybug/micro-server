package com.aol.micro.server.spring.metrics;


import com.aol.micro.server.Plugin;
import com.aol.micro.server.spring.metrics.health.HealthCheckRunner;
import com.aol.micro.server.spring.metrics.health.HealthResource;
import cyclops.collections.immutable.PersistentSetX;

/**
 * 
 * Collections of Spring configuration classes (Classes annotated with @Configuration)
 * that configure various useful pieces of functionality - such as property file loading,
 * datasources, scheduling etc
 * 
 * @author johnmcclean
 *
 */
public class MetricsPlugin implements Plugin {

    @Override
    public PersistentSetX<Class> springClasses() {
        return PersistentSetX.of(CodahaleMetricsConfigurer.class, HealthCheckRunner.class, HealthResource.class);
    }

}
