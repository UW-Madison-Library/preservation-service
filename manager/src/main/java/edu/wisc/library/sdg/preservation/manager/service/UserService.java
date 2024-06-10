package edu.wisc.library.sdg.preservation.manager.service;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUser;
import edu.wisc.library.sdg.preservation.manager.db.model.UserOrgMap;
import edu.wisc.library.sdg.preservation.manager.db.model.VaultPermission;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationUserRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.UserOrgMapRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultPermissionRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PreservationUserRepository userRepo;
    private final UserOrgMapRepository userOrgMapRepo;
    private final VaultPermissionRepository vaultPermissionRepo;
    private final VaultRepository vaultRepository;

    @Autowired
    public UserService(PreservationUserRepository userRepo,
                       UserOrgMapRepository userOrgMapRepo,
                       VaultPermissionRepository vaultPermissionRepo,
                       VaultRepository vaultRepository) {
        this.userRepo = userRepo;
        this.userOrgMapRepo = userOrgMapRepo;
        this.vaultPermissionRepo = vaultPermissionRepo;
        this.vaultRepository = vaultRepository;
    }

    // TODO this needs to be cached
    public PreservationUser getByUsername(String username) {
        return userRepo.findById(username).orElseThrow(() ->
                new NotFoundException(String.format("User %s was not found", username)));
    }

    /**
     * Returns all preservation users
     *
     * @return list of all users
     */
    public List<PreservationUser> getAll() {
        return userRepo.findAll();
    }

    /**
     * Returns the user associated with the specified api key
     *
     * @param apiKey the api key
     * @return the user associated with the key
     */
    public PreservationUser getUserByApiKey(String apiKey) {
        var encodedKey = DigestUtils.sha256Hex(apiKey);
        return userRepo.findByApiKeyHash(encodedKey);
    }

    /**
     * Returns the user associated with the external id
     *
     * @param externalId the external id
     * @return the user associated with the id
     */
    public PreservationUser getUserByExternalId(String externalId) {
        return userRepo.findByExternalId(externalId);
    }

    /**
     * Returns the details about a user's association to an org, if it is associated. This INCLUDES users that
     * are associated but not enabled
     *
     * @param username the username
     * @param orgName the org name
     * @return user org details, or none
     */
    public Optional<UserOrgMap> getOrgUser(String username, String orgName) {
        return userOrgMapRepo.findByUsernameAndOrgName(username, orgName);
    }

    /**
     * Sets a user's vault permissions. This overwrites any existing permissions. If no permissions are provided,
     * then any existing permissions are removed.
     *
     * @param username the user's username
     * @param vault the vault to apply the permissions to
     * @param permissions the permissions
     */
    @Transactional
    public void setVaultPermissions(String username, String vault, Set<Permission> permissions) {
        ArgCheck.notBlank(username, "username");
        ArgCheck.notBlank(vault, "vault");

        vaultPermissionRepo.deleteAllByUsernameAndVault(username, vault);

        if (!(permissions == null || permissions.isEmpty())) {
            var vaultPermissions = permissions.stream().map(permission -> {
                return new VaultPermission()
                        .setVault(vault)
                        .setUsername(username)
                        .setPermission(permission);
            }).collect(Collectors.toList());

            vaultPermissionRepo.saveAll(vaultPermissions);
        }
    }

    /**
     * Lists all of the vault permissions associated to a user in an org
     *
     * @param username the user's username
     * @param orgName the org to query
     * @return list of vault permissions
     */
    public List<VaultPermission> listUserOrgVaultPermissions(String username, String orgName) {
        ArgCheck.notBlank(username, "username");
        ArgCheck.notBlank(orgName, "orgName");

        return vaultPermissionRepo.findByUsernameAndOrgName(username, orgName);
    }

    /**
     * Lists a user's permissions for a vault
     *
     * @param username the user's username
     * @param vault the name of the vault
     * @return list of vault permissions
     */
    public List<VaultPermission> listUserVaultPermissions(String username, String vault) {
        ArgCheck.notBlank(username, "username");
        ArgCheck.notBlank(vault, "vault");

        return vaultPermissionRepo.findByUsernameAndVault(username, vault);
    }
}
