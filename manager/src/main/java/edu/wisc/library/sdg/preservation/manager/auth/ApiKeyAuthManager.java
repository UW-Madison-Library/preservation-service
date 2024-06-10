package edu.wisc.library.sdg.preservation.manager.auth;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Authenticates API keys. The API key must be associated to an enabled user. If the user is a "PROXYING_USER"
 * then they must be proxying the request on behalf of another user. The user they are proxying must be
 * an existing enabled user. The proxied user is specified by their external id.
 */
public class ApiKeyAuthManager implements AuthenticationManager {

    private static final Logger LOG = LoggerFactory.getLogger(ApiKeyAuthManager.class);

    private final UserService userService;

    public ApiKeyAuthManager(UserService userService) {
        this.userService = ArgCheck.notNull(userService, "userService");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var principal = authentication.getPrincipal();

        if (!(principal instanceof ApiKeyAuthToken)) {
            throw new BadCredentialsException("Missing authentication token");
        }

        var apiKey = (ApiKeyAuthToken) principal;

        if (apiKey.getApiKey() == null) {
            throw new BadCredentialsException("Missing authentication token");
        }

        var user = userService.getUserByApiKey(apiKey.getApiKey());

        LOG.debug("Found user: {}", user);

        if (user == null) {
            throw new BadCredentialsException("Authentication failed");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if (user.getUserType() == PreservationUserType.PROXYING_USER) {
            if (apiKey.getProxyFor() != null) {
                LOG.debug("Attempting to proxy as user: {}", apiKey.getProxyFor());

                var proxyUser = userService.getUserByExternalId(apiKey.getProxyFor());

                LOG.debug("Found proxy user: {}", proxyUser);

                if (proxyUser == null) {
                    throw new BadCredentialsException("Proxy user not found");
                }

                if (!proxyUser.isEnabled()) {
                    throw new DisabledException("Proxy user is disabled");
                }

                if (proxyUser.getUserType() == PreservationUserType.PROXYING_USER) {
                    throw new BadCredentialsException("Cannot proxy another proxying user");
                }

                user = proxyUser;
            } else {
                throw new BadCredentialsException("Must include a proxy user");
            }
        }

        return new UserAuthToken(new PreservationUserDetails(user));
    }

}
