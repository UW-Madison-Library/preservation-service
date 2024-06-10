package edu.wisc.library.sdg.preservation.manager.service;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgUserComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.Organization;
import edu.wisc.library.sdg.preservation.manager.db.model.Vault;
import edu.wisc.library.sdg.preservation.manager.db.model.VaultUserComposite;
import edu.wisc.library.sdg.preservation.manager.db.repo.OrganizationRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.UserOrgMapRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultPermissionRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrganizationService {

    private final VaultRepository vaultRepo;
    private final OrganizationRepository organizationRepo;
    private final UserOrgMapRepository userOrgMapRepo;
    private final VaultPermissionRepository vaultPermissionRepo;

    @Autowired
    public OrganizationService(VaultRepository vaultRepo,
                               OrganizationRepository organizationRepo,
                               UserOrgMapRepository userOrgMapRepo,
                               VaultPermissionRepository vaultPermissionRepo) {
        this.vaultRepo = vaultRepo;
        this.organizationRepo = organizationRepo;
        this.userOrgMapRepo = userOrgMapRepo;
        this.vaultPermissionRepo = vaultPermissionRepo;
    }

    public List<Vault> getVaults(String orgName) {
        ArgCheck.notBlank(orgName, "orgName");
        return vaultRepo.findAllByOrgName(orgName);
    }

    public void createVault(String name, String description, String orgName) {
        ArgCheck.notBlank(name, "name");
        ArgCheck.notBlank(description, "description");
        ArgCheck.notBlank(orgName, "orgName");
        var now = Time.now();

        var vault = new Vault()
                .setName(name)
                .setDescription(description)
                .setOrgName(orgName)
                .setCreatedTimestamp(now)
                .setUpdatedTimestamp(now)
                .setNew(true);
        try {
            vaultRepo.save(vault);
        } catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                throw new ValidationException(String.format(
                        "Vaults must be globally unique. <%s> is already in use.",
                        vault.getName()));
            } else {
                throw e;
            }
        }
    }

    public Vault getVault(String name) {
        ArgCheck.notBlank(name, "name");

        return vaultRepo.findById(name).orElseThrow(() ->
                new NotFoundException(String.format("Vault <%s> was not found.", name)));
    }

    public List<OrgUserComposite> getUsersInOrg(String orgName) {
        ArgCheck.notBlank(orgName, "orgName");

        return userOrgMapRepo.findAllGloballyEnabledByOrgName(orgName);
    }

    public List<VaultUserComposite> getUsersWithVaultAccess(String vault) {
        ArgCheck.notBlank(vault, "vault");

        var users = vaultPermissionRepo.findAllGloballyEnabledUsersWithAccessToVault(vault);
        var userMap = new HashMap<String, VaultUserComposite>(users.size());

        // The query returns multiple results per users if they have multiple permissions. This collapses them
        users.forEach(user -> {
            var u = userMap.computeIfAbsent(user.getUsername(), k -> user);
            u.addPermission(user.getPermission());
            u.setPermission(null);
        });

        return new ArrayList<>(userMap.values());
    }

    public List<Organization> getUserOrgs(String username) {
        ArgCheck.notBlank(username, "username");

        return organizationRepo.findAllByUser(username);
    }

    public Organization getOrganization(String orgName) {
        ArgCheck.notBlank(orgName, "orgName");

        return organizationRepo.findById(orgName)
                .orElseThrow(() -> new NotFoundException(String.format("Organization %s was not found.", orgName)));
    }

    public Organization createOrganization(String orgName,
                                           String orgDisplayName,
                                           String contactName,
                                           String contactEmail,
                                           String contactPhone) {

        var now = Time.now();

        var organization = new Organization()
                .setOrgName(orgName)
                .setDisplayName(orgDisplayName)
                .setContactName(contactName)
                .setContactEmail(contactEmail)
                .setContactPhone(contactPhone)
                .setCreatedTimestamp(now)
                .setUpdatedTimestamp(now)
                .setNew(true);
        try {
            return organizationRepo.save(organization);
        } catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                throw new ValidationException(String.format(
                        "Organization names must be globally unique. <%s> is already in use.",
                        organization.getOrgName()));
            } else {
                throw e;
            }
        }
    }

    public List<Organization> getAllOrganizations() {
        var orgs = new ArrayList<Organization>();
        organizationRepo.findAll().forEach(orgs::add);
        return orgs;
    }

    public OrgUserComposite getOrgUser(String orgName, String username) {
        ArgCheck.notBlank(orgName, "orgName");
        ArgCheck.notBlank(username, "username");

        return userOrgMapRepo.findByOrgNameAndUsername(orgName, username);
    }

    /**
     * @param orgName the org to filter on
     * @return total number of objects in all vaults in org
     */
    public long getObjectCountByOrg(String orgName) {
        return vaultRepo.countAllObjectsByOrg(orgName).orElse(0L);
    }

    /**
     * @param orgName the org to filter on
     * @return total number of MBs across all vaults in org
     */
    public long getTotalStorageMbByOrg(String orgName) {
        return vaultRepo.sumObjectBytesInMbByOrg(orgName).orElse(0L);
    }

    /**
     * @return total number of objects in all vaults
     */
    public long getObjectCount() {
        return vaultRepo.countAllObjects().orElse(0L);
    }

    /**
     * @return total number of MBs across all vaults
     */
    public long getTotalStorageMb() {
        return vaultRepo.sumObjectBytesInMb().orElse(0L);
    }

}
