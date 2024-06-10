package edu.wisc.library.sdg.preservation.manager.auth;

import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.repo.UserOrgMapRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultPermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PreservationAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(PreservationAuthService.class);

    private final VaultPermissionRepository vaultPermissionRepo;
    private final UserOrgMapRepository userOrgMapRepository;

    @Autowired
    public PreservationAuthService(VaultPermissionRepository vaultPermissionRepo,
                                   UserOrgMapRepository userOrgMapRepository) {
        this.vaultPermissionRepo = vaultPermissionRepo;
        this.userOrgMapRepository = userOrgMapRepository;
    }

    public boolean hasVaultPermission(UserDetails user, String vault, Permission permission) {
        if (vault == null) {
            return false;
        }

        LOG.debug("Permission check: vault=<{}>, user=<{}>, permission=<{}>", vault, user, permission);

        if (!user.isEnabled()) {
            return false;
        }

        if (AuthUtil.isServiceAdmin(user.getAuthorities())) {
            return true;
        }

        var vaultPermission = vaultPermissionRepo.findByVaultAndUsernameAndPermission(
                vault, user.getUsername(), permission);

        return vaultPermission.isPresent();
    }

    public boolean hasOrgAuthority(UserDetails user, String orgName, OrgAuthority authority) {
        if (orgName == null) {
            return false;
        }

        LOG.debug("Permission check: org=<{}>, user=<{}>, authority=<{}>", orgName, user, authority);

        if (!user.isEnabled()) {
            return false;
        }

        if (AuthUtil.isServiceAdmin(user.getAuthorities())) {
            return true;
        }

        var orgUserOpt = userOrgMapRepository.findByUsernameAndOrgName(user.getUsername(), orgName);

        LOG.debug("OrgUser: {}", orgUserOpt);

        if (orgUserOpt.isPresent()) {
            var orgUser = orgUserOpt.get();
            return orgUser.isEnabled() && orgUser.getRole().hasAuthority(authority);
        }

        return false;
    }

    public boolean isServiceAdmin(UserDetails user) {
        LOG.debug("Service admin check: user=<{}>", user);

        if (!user.isEnabled()) {
            return false;
        }

        return AuthUtil.isServiceAdmin(user.getAuthorities());
    }

}
