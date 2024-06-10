package edu.wisc.library.sdg.preservation.manager.auth;

/**
 * Encapsulates an API key and optional proxy user. These values have not been authenticated
 */
public class ApiKeyAuthToken {

    private final String apiKey;
    private final String proxyFor;

    public ApiKeyAuthToken(String apiKey, String proxyFor) {
        this.apiKey = apiKey;
        this.proxyFor = proxyFor;
    }

    /**
     * @return the api key associated with the request
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @return the external id of the user that is being proxied, optional
     */
    public String getProxyFor() {
        return proxyFor;
    }

}
