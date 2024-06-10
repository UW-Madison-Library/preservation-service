package edu.wisc.library.sdg.preservation.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Function;

public final class RestTemplates {

    private static final String PROXY_HEADER = "X-PROXY-FOR";

    private RestTemplates() {

    }

    public static RestTemplate restTemplate(Path tempDir, ObjectMapper objectMapper) {
        return restTemplate(tempDir, objectMapper, null);
    }

    public static RestTemplate restTemplate(String username,
                                            String password,
                                            Path tempDir,
                                            ObjectMapper objectMapper) {
        return restTemplate(tempDir, objectMapper, builder -> {
            return builder.basicAuthentication(username, password);
        });
    }

    public static RestTemplate restTemplate(String proxyUser,
                                            Path tempDir,
                                            ObjectMapper objectMapper) {
        return restTemplate(tempDir, objectMapper, builder -> {
            return builder.defaultHeader(PROXY_HEADER, proxyUser);
        });
    }

    public static RestTemplate restTemplate(Path tempDir,
                                            ObjectMapper objectMapper,
                                            Function<RestTemplateBuilder, RestTemplateBuilder> configurer) {
        // This nonsense is necessary so that our file converter has precedence
        var defaultConverters = new RestTemplate().getMessageConverters();
        var converters = new ArrayList<HttpMessageConverter<?>>(defaultConverters.size() + 1);
        converters.add(new FileMessageConverter(tempDir));
        converters.addAll(defaultConverters);

        var builder = new RestTemplateBuilder()
                .additionalMessageConverters(converters)
                .errorHandler(new ExceptionResponseErrorHandler(objectMapper));

        if (configurer != null) {
            builder = configurer.apply(builder);
        }

        return builder.build();
    }

}
