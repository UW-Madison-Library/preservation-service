package edu.wisc.library.sdg.preservation.worker;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        SqlInitializationAutoConfiguration.class
})
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "edu\\.wisc\\.library\\.sdg\\.preservation\\.common\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "edu\\.wisc\\.library\\.sdg\\.preservation\\.worker\\.client\\..*"),
})
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            LoggerFactory.getLogger(Application.class).error("Failure during spring initialization. Exiting");
            System.exit(1);
        }
    }

}

