package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.exception.AccessDeniedException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateVaultRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.UpdateUserVaultPermissionsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.UserType;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultObject;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermission;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermissions;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermissionsWithDescription;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VaultITest extends ITestBase {

    public static final String DEFAULT_DESC = "A vault for tests";

    @Test
    public void listVaults() {
        var vaultObject = new VaultObject();
        vaultObject.setName(DEFAULT_VAULT);
        vaultObject.setDescription(DEFAULT_DESC);

        var vaults = regularUserClient.listVaults(TEST_ORG).getVaults();
        assertEquals(vaults.size(), 1);
        assertThat(vaults, containsInAnyOrder(vaultObject));
    }

    @Test
    public void createVaultAsAdminUser() {
        var ns = createVault("new-vault", "a new vault");

        var vaults = adminUserClient.listVaults(TEST_ORG).getVaults();
        assertThat(vaults, hasItem(ns));
    }

    @Test
    public void createVaultAsServiceAdminUser() {
        var ns = new VaultObject().name("new-vault").description("a new vault");
        serviceAdminUserClient.createVault(new CreateVaultRequest()
                .orgName(TEST_ORG)
                .vault(ns));

        var vaults = serviceAdminUserClient.listVaults(TEST_ORG).getVaults();
        assertThat(vaults, hasItem(ns));
    }

    @Test
    public void createVaultWithSpace() {
        var ns = createVault("new vault", "a new vault");

        var vaults = adminUserClient.listVaults(TEST_ORG).getVaults();
        assertThat(vaults, hasItem(ns));
    }

    @Test
    public void failCreateVaultAsRegularUser() {
        var vault = "new-vault";

        var vaultObject = new VaultObject();
        vaultObject.setName(vault);
        vaultObject.setDescription("a new vault");

        assertThrows(AccessDeniedException.class, () -> {
            regularUserClient
                    .createVault(new CreateVaultRequest().orgName(TEST_ORG).vault(vaultObject));
        });

        var vaults = regularUserClient.listVaults(TEST_ORG).getVaults();
        assertThat(vaults, not(hasItem(vaultObject)));
    }

    @Test
    public void failCreateDuplicateVault() {
        assertThat(assertThrows(ValidationException.class, () -> {
            createVault(DEFAULT_VAULT, "duplicate of default vault");
        }).getMessage(), containsString("must be globally unique"));
    }

    @Test
    public void failCreateShortVault() {
        assertThat(assertThrows(ValidationException.class, () -> {
            createVault("new", "this vault is too short");
        }).getMessage(), containsString("4-128 characters long"));
    }

    @Test
    public void failCreateLongVault() {
        var vault = "toolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoo";

        assertThat(assertThrows(ValidationException.class, () -> {
            createVault(vault, "this vault is too long");
        }).getMessage(), containsString("4-128 characters long"));
    }

    @Test
    public void failCreateUnallowedVault() {
        assertThat(assertThrows(ValidationException.class, () -> {
            createVault("%vault", "this vault is not allowed");
        }).getMessage(), containsString("Allowed special characters are: -_"));
    }

    @Test
    public void listVaultsPermissions() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        var adminPerms = adminUserClient.listOrgUserVaultPermissions(TEST_ORG, null)
                .getVaultPermissions();
        var userPerms = adminUserClient.listOrgUserVaultPermissions(TEST_ORG, USER_1_USER)
                .getVaultPermissions();

        assertThat(adminPerms, containsInAnyOrder(
                vaultPermWithDesc(newVault, "a new vault", VaultPermission.READ, VaultPermission.WRITE),
                vaultPermWithDesc(DEFAULT_VAULT, DEFAULT_DESC, VaultPermission.READ, VaultPermission.WRITE)));

        assertThat(userPerms, containsInAnyOrder(
                vaultPermWithDesc(newVault, "a new vault"),
                vaultPermWithDesc(DEFAULT_VAULT, DEFAULT_DESC, VaultPermission.READ, VaultPermission.WRITE)));
    }

    @Test
    public void listUsersInVault() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        var users = adminUserClient.listUsersInVault(newVault).getUsers();

        assertEquals(1, users.size());

        var user = users.get(0);
        assertEquals(ADMIN_1_USER, user.getUsername());
        assertEquals("Admin 1", user.getDisplayName());
        assertEquals(UserType.GENERAL_USER, user.getUserType());
        assertTrue(user.getEnabledInOrg());

        user.getPermissions().sort(Comparator.naturalOrder());

        assertThat(user.getPermissions(), containsInAnyOrder(VaultPermission.READ, VaultPermission.WRITE));
    }

    @Test
    public void describeVaultWithPermissions() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        var response = adminUserClient.describeVault(newVault);

        assertEquals(newVault, response.getVault().getName());
        assertEquals("a new vault", response.getVault().getDescription());
        assertEquals(TEST_ORG, response.getOrgName());
        response.getPermissions().sort(Comparator.naturalOrder());
        assertThat(response.getPermissions(), containsInAnyOrder(VaultPermission.READ, VaultPermission.WRITE));
    }

    @Test
    public void describeVaultNoPermissions() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        var response = regularUserClient.describeVault(newVault);

        assertEquals(newVault, response.getVault().getName());
        assertEquals("a new vault", response.getVault().getDescription());
        assertEquals(TEST_ORG, response.getOrgName());
        assertThat(response.getPermissions(), empty());
    }

    @Test
    public void describeVaultCounts() {
        defaultObject1 = new TestObject("o1")
                .addFileWithFormat("MASTER0", 100L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "image/tiff"))
                .addFileWithFormat("BIB0", 50L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));
        defaultObject1v2 = new TestObject("o1")
                .version(2)
                .addFileWithFormat("MASTER0", 100L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "image/tiff"))
                .addFileWithFormat("BIB0", 100L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));
        defaultObject1v2.internalId = defaultObject1.internalId;
        defaultObject2 = new TestObject("o2")
                .addFileWithFormat("ENCTEXT", 200L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"))
                .addFileWithFormat("TECH0", 20L * 1024 * 512,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));

        setupBaseline();

        var response = adminUserClient.describeVault(DEFAULT_VAULT);

        assertEquals(2, response.getObjectCount());
        assertEquals(235, response.getApproximateStorageMegabytes());
    }

    @Test
    public void addVaultPermsToUser() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        adminUserClient.updateUserVaultPermissions(new UpdateUserVaultPermissionsRequest()
                .username(USER_1_USER)
                .vaultPermissions(new VaultPermissions()
                        .vault(newVault)
                        .addPermissionsItem(VaultPermission.READ)));

        var userPerms = adminUserClient.listOrgUserVaultPermissions(TEST_ORG, USER_1_USER)
                .getVaultPermissions();

        assertThat(userPerms, containsInAnyOrder(
                vaultPermWithDesc(newVault, "a new vault", VaultPermission.READ),
                vaultPermWithDesc(DEFAULT_VAULT, DEFAULT_DESC, VaultPermission.READ, VaultPermission.WRITE)));
    }

    @Test
    public void removeVaultPermsFromUser() {
        adminUserClient.updateUserVaultPermissions(new UpdateUserVaultPermissionsRequest()
                .username(USER_1_USER)
                .vaultPermissions(new VaultPermissions()
                        .vault(DEFAULT_VAULT)));

        var userPerms = adminUserClient.listOrgUserVaultPermissions(TEST_ORG, USER_1_USER)
                .getVaultPermissions();

        assertThat(userPerms, containsInAnyOrder(
                vaultPermWithDesc(DEFAULT_VAULT, DEFAULT_DESC)));
    }

    @Test
    public void rejectVaultPermissionsChangeWhenNotAdmin() {
        var newVault = "new-vault";
        createVault(newVault, "a new vault");

        assertThrows(AccessDeniedException.class, () -> {
            regularUserClient.updateUserVaultPermissions(new UpdateUserVaultPermissionsRequest()
                    .username(USER_1_USER)
                    .vaultPermissions(new VaultPermissions()
                            .vault(newVault)
                            .addPermissionsItem(VaultPermission.READ)));
        });
    }

    private VaultObject createVault(String name, String description) {
        var ns = new VaultObject().name(name).description(description);
        adminUserClient.createVault(new CreateVaultRequest()
                .orgName(TEST_ORG)
                .vault(ns));
        return ns;
    }

    private VaultPermissions vaultPerm(String vault, VaultPermission... permissions) {
        var vaultPerm = new VaultPermissions().vault(vault);

        if (permissions != null && permissions.length > 0) {
            for (var perm : permissions) {
                vaultPerm.addPermissionsItem(perm);
            }
        } else {
            vaultPerm.setPermissions(Collections.emptyList());
        }

        return vaultPerm;
    }

    private VaultPermissionsWithDescription vaultPermWithDesc(String vault, String desc, VaultPermission... permissions) {
        var vaultPerm = new VaultPermissionsWithDescription()
                .vault(new VaultObject().name(vault).description(desc));

        if (permissions != null && permissions.length > 0) {
            for (var perm : permissions) {
                vaultPerm.addPermissionsItem(perm);
            }
        } else {
            vaultPerm.setPermissions(Collections.emptyList());
        }

        return vaultPerm;
    }

}
