package edu.wisc.library.sdg.preservation.manager.auth;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUser;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ApiKeyAuthManagerTest {

    @Mock
    private UserService userService;

    private ApiKeyAuthManager authManager;

    @BeforeEach
    public void setup() {
        authManager = new ApiKeyAuthManager(userService);
    }

    @Test
    public void failAuthWhenTokenNull() {
        assertThrows(BadCredentialsException.class, () -> {
            authManager.authenticate(new PreAuthenticatedAuthenticationToken(null, null));
        });
    }

    @Test
    public void failAuthWhenApiKeyNull() {
        assertThrows(BadCredentialsException.class, () -> {
            authManager.authenticate(authentication(null, null));
        });
    }

    @Test
    public void passAuthWhenApiKeyValidNormalUser() {
        var key = "asdf1";
        var user = "user2";

        doReturn(user(user, PreservationUserType.GENERAL_USER, true))
                .when(userService).getUserByApiKey(key);

        var auth = authManager.authenticate(authentication(key, null));

        assertEquals(user, ((UserDetails) auth.getPrincipal()).getUsername());
    }

    @Test
    public void failAuthWhenApiKeyValidNormalUserDisabled() {
        var key = "asdf2";
        var user = "user2";

        doReturn(user(user, PreservationUserType.GENERAL_USER, false))
                .when(userService).getUserByApiKey(key);

        assertThrows(DisabledException.class, () -> {
            authManager.authenticate(authentication(key, null));
        });
    }

    @Test
    public void failAuthWhenNoUserFound() {
        var key = "asdf3";

        doReturn(null).when(userService).getUserByApiKey(key);

        assertThrows(BadCredentialsException.class, () -> {
            authManager.authenticate(authentication(key, null));
        });
    }

    @Test
    public void failAuthWhenApiKeyValidSuperWithNoProxy() {
        var key = "asdf3";
        var user = "user3";

        doReturn(user(user, PreservationUserType.PROXYING_USER, true))
                .when(userService).getUserByApiKey(key);

        assertThrows(BadCredentialsException.class, () -> {
            authManager.authenticate(authentication(key, null));
        });
    }

    @Test
    public void failAuthWhenApiKeyValidSuperButDisabled() {
        var key = "asdf4";
        var user = "user4";
        var proxy = "proxy";

        doReturn(user(user, PreservationUserType.PROXYING_USER, false))
                .when(userService).getUserByApiKey(key);

        assertThrows(DisabledException.class, () -> {
            authManager.authenticate(authentication(key, proxy));
        });
    }

    @Test
    public void failAuthWhenApiKeyValidSuperButProxyDisabled() {
        var key = "asdf5";
        var user = "user5";
        var proxy = "proxy";
        var proxyName = "proxyName";

        doReturn(user(user, PreservationUserType.PROXYING_USER, true))
                .when(userService).getUserByApiKey(key);
        doReturn(user(proxyName, PreservationUserType.GENERAL_USER, false))
                .when(userService).getUserByExternalId(proxy);

        assertThrows(DisabledException.class, () -> {
            authManager.authenticate(authentication(key, proxy));
        });
    }

    @Test
    public void failAuthWhenApiKeyValidSuperButProxyIsSuper() {
        var key = "asdf6";
        var user = "user6";
        var proxy = "proxy1";
        var proxyName = "proxyName1";

        doReturn(user(user, PreservationUserType.PROXYING_USER, true))
                .when(userService).getUserByApiKey(key);
        doReturn(user(proxyName, PreservationUserType.PROXYING_USER, true))
                .when(userService).getUserByExternalId(proxy);

        assertThrows(BadCredentialsException.class, () -> {
            authManager.authenticate(authentication(key, proxy));
        });
    }

    @Test
    public void passAuthWheApiKeyValidSuperWithValidProxy() {
        var key = "asdf7";
        var user = "user7";
        var proxy = "proxy2";
        var proxyName = "proxyName2";

        doReturn(user(user, PreservationUserType.PROXYING_USER, true))
                .when(userService).getUserByApiKey(key);
        doReturn(user(proxyName, PreservationUserType.GENERAL_USER, true))
                .when(userService).getUserByExternalId(proxy);

        var auth = authManager.authenticate(authentication(key, proxy));

        assertEquals(proxyName, ((UserDetails) auth.getPrincipal()).getUsername());
    }

    private Authentication authentication(String apiKey, String proxyFor) {
        return new PreAuthenticatedAuthenticationToken(new ApiKeyAuthToken(apiKey, proxyFor), null);
    }

    private PreservationUser user(String username, PreservationUserType type, boolean enabled) {
        return new PreservationUser().setUsername(username).setUserType(type).setEnabled(enabled);
    }

}
