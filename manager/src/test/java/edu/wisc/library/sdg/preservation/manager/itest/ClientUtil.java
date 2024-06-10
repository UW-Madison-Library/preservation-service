package edu.wisc.library.sdg.preservation.manager.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.wisc.library.sdg.preservation.common.client.RestTemplates;
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Paths;

public final class ClientUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String PUBLIC_API = "http://localhost:%s/api";
    private static final String INTERNAL_API = "http://localhost:%s/internal/api";

    public static final String INTERNAL_USER = "prsWkrLocal";
    public static final String INTERNAL_PASSWORD = "yKsQuNKGo0fGyKanPGtv";

    public static final String FEDORA_USER = "fedora-object-preserver";
    public static final String FEDORA_API_KEY = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

    public static final String ADMIN_APP_API_KEY = "e1d14690ec5c01ab456a5ecd900fc81a690f5c8363d2fb353ddb9ff9cbfa4288";

    private ClientUtil() {

    }

    public static PreservationManagerApi defaultPublicManagerClient(int port) {
        return publicManagerClient(port, FEDORA_API_KEY);
    }

    public static PreservationManagerApi proxyingManagerClient(int port, String proxyUser) {
        var restTemplate = RestTemplates.restTemplate(proxyUser, Paths.get("target/tmp"), objectMapper);
        return createPublicManagerClient(port, ADMIN_APP_API_KEY, restTemplate);
    }

    public static PreservationManagerApi publicManagerClient(int port, String apiKey) {
        var restTemplate = RestTemplates.restTemplate(Paths.get("target/tmp"), objectMapper);
        return createPublicManagerClient(port, apiKey, restTemplate);
    }

    private static PreservationManagerApi createPublicManagerClient(int port, String apiKey, RestTemplate restTemplate) {
        var apiClient = new ApiClient(restTemplate);
        apiClient.setApiKey(apiKey);
        apiClient.setBasePath(String.format(PUBLIC_API, port));
        return new PreservationManagerApi(apiClient);
    }

    public static ManagerInternalApi defaultInternalManagerClient(int port) {
        return internalManagerClient(port, INTERNAL_USER, INTERNAL_PASSWORD);
    }

    public static ManagerInternalApi internalManagerClient(int port, String username, String password) {
        var restTemplate = RestTemplates.restTemplate(username, password, Paths.get("target/tmp"), objectMapper);
        var apiClient = new edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient(restTemplate);
        apiClient.setBasePath(String.format(INTERNAL_API, port));
        return new ManagerInternalApi(apiClient);
    }

}
