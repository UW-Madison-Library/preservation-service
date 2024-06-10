package edu.wisc.library.sdg.preservation.manager.auth;

import com.google.common.base.Strings;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public class PreservationAuth {

    /**
     * Executes the runnable only if the authenticated user has the specified permission for the specified vault.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.hasVaultPermission(principal, #vault, #permission)")
    public void apply(String vault, Permission permission, Runnable runnable) {
        if (Strings.isNullOrEmpty(vault)) {
            throw new ValidationException("Vault cannot be blank.");
        }
        runnable.run();
    }

    /**
     * Executes the callable only if the authenticated user has the specified permission for the specified vault.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.hasVaultPermission(principal, #vault, #permission)")
    public <T> T apply(String vault, Permission permission, Callable<T> callable) {
        if (Strings.isNullOrEmpty(vault)) {
            throw new ValidationException("Vault cannot be blank.");
        }
        try {
            return callable.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the runnable only if the authenticated user has the specified authority in the organization.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.hasOrgAuthority(principal, #orgName, #authority)")
    public void apply(String orgName, OrgAuthority authority, Runnable runnable) {
        if (Strings.isNullOrEmpty(orgName)) {
            throw new ValidationException("orgName cannot be blank.");
        }
        runnable.run();
    }

    /**
     * Executes the callable only if the authenticated user has the specified authority in the organization.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.hasOrgAuthority(principal, #orgName, #authority)")
    public <T> T apply(String orgName, OrgAuthority authority, Callable<T> callable) {
        if (Strings.isNullOrEmpty(orgName)) {
            throw new ValidationException("orgName cannot be blank.");
        }
        try {
            return callable.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the callable only if the authenticated user is a service admin.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.isServiceAdmin(principal)")
    public <T> T applyIfServiceAdmin(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the runnable only if the authenticated user is a service admin.
     * If the user does not have permission, an exception is thrown.
     */
    @PreAuthorize("@preservationAuthService.isServiceAdmin(principal)")
    public void applyIfServiceAdmin(Runnable runnable) {
        runnable.run();
    }

}
