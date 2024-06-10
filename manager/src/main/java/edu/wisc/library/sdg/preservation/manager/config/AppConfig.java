package edu.wisc.library.sdg.preservation.manager.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import edu.wisc.library.sdg.preservation.common.spring.GlobalExceptionHandler;
import edu.wisc.library.sdg.preservation.common.spring.RequestIdFilter;
import edu.wisc.library.sdg.preservation.common.spring.RequestLoggingFilter;
import edu.wisc.library.sdg.preservation.manager.job.DefaultJobBroker;
import edu.wisc.library.sdg.preservation.manager.job.DefaultJobBrokerLifecycle;
import edu.wisc.library.sdg.preservation.manager.job.SimpleJobBroker;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Value("${app.jobs.queue.capacity.total}")
    private int jobQueueCapacity;

    @Value("${app.jobs.queue.capacity.background-percent}")
    private double jobQueueBackgroundPercent;

    @Bean
    @Order(1)
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    @Order(2)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public RequestLoggingFilter logFilter() {
        var filter = new RequestLoggingFilter();
        filter.setExcludePayloadPaths(Set.of("/api/ingest/bag"));
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        return filter;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public ExecutorService eventBusExecutor() {
        // Spring will handle shutdown
        return Executors.newCachedThreadPool();
    }

    @Bean
    @Profile({"!itest"})
    public DefaultJobBroker jobBroker(JobService jobService) {
        return new DefaultJobBroker(jobService, jobQueueCapacity, jobQueueBackgroundPercent);
    }

    @Bean
    @Profile({"!itest"})
    public DefaultJobBrokerLifecycle jobBrokerLifecycle(DefaultJobBroker jobBroker) {
        return new DefaultJobBrokerLifecycle(jobBroker);
    }

    @Bean
    @Profile({"itest"})
    public SimpleJobBroker testJobBroker(JobService jobService) {
        return new SimpleJobBroker(jobService);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper(DozerBeanMapperBuilder.create()
                .withMappingFiles("mapping.xml")
                .build());
    }

}
