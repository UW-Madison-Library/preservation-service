package edu.wisc.library.sdg.preservation.manager.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Simple auth token impl that just wraps a user details object
 */
public class UserAuthToken extends AbstractAuthenticationToken {

    private final UserDetails username;

    public UserAuthToken(UserDetails username) {
        super(username.getAuthorities());
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
