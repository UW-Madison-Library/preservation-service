package edu.wisc.library.sdg.preservation.common.spring;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;

/**
 * Extension of CommonsRequestLoggingFilter to allow excluding the request payloads of some requests
 */
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    private Set<String> excludePayloadPaths = Collections.emptySet();

    /**
     * @param excludePayloadPaths paths to exclude when logging request payloads
     */
    public void setExcludePayloadPaths(Set<String> excludePayloadPaths) {
        this.excludePayloadPaths = excludePayloadPaths == null ? Collections.emptySet() : excludePayloadPaths;
    }

    @Override
    protected String getMessagePayload(HttpServletRequest request) {
        if (excludePayloadPaths.contains(request.getRequestURI())) {
            return null;
        }
        return super.getMessagePayload(request);
    }
}
