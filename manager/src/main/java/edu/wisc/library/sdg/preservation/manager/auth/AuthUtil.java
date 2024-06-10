package edu.wisc.library.sdg.preservation.manager.auth;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public final class AuthUtil {

    private static final GrantedAuthority SERVICE_ADMIN_AUTHORITY = new SimpleGrantedAuthority(PreservationUserType.SERVICE_ADMIN.name());

    private AuthUtil() {

    }

    public static boolean isServiceAdmin(Authentication authentication) {
        return isServiceAdmin(authentication.getAuthorities());
    }

    public static boolean isServiceAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.contains(SERVICE_ADMIN_AUTHORITY);
    }

}
