package edu.wisc.library.sdg.preservation.manager.auth;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * This filter looks for the X-API-KEY and X-PROXY-FOR headers and authenticates requests
 * that contain them.
 */
public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String PROXY_HEADER = "X-PROXY-FOR";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        var apiKey = httpServletRequest.getHeader(API_KEY_HEADER);
        var proxyFor = httpServletRequest.getHeader(PROXY_HEADER);

        if (apiKey == null) {
            return null;
        }

        return new ApiKeyAuthToken(apiKey, proxyFor);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
