package edu.wisc.library.sdg.preservation.manager.auth;

import edu.wisc.library.sdg.preservation.manager.config.SecurityConfig;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Simple UserDetails implementation that just includes the authenticated user's name
 */
public class PreservationUserDetails implements UserDetails {

    private final String username;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public PreservationUserDetails(PreservationUser user) {
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.authorities = List.of(
                // this authority is needed in the spring security filter chain
                new SimpleGrantedAuthority(SecurityConfig.PUBLIC_API_AUTHORITY),
                new SimpleGrantedAuthority(user.getUserType().name())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreservationUserDetails that = (PreservationUserDetails) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "PreservationUserDetails{" +
                "username='" + username + '\'' +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }
}
