package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.exception.AccessDeniedException;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateOrgRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.client.model.OrgUser;
import edu.wisc.library.sdg.preservation.manager.client.model.Role;
import edu.wisc.library.sdg.preservation.manager.client.model.UserType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrgITest extends ITestBase {

    @Test
    public void listAllUsersInOrg() {
        var result = adminUserClient.listOrgUsers(TEST_ORG);

        assertThat(result.getUsers(), containsInAnyOrder(
                new OrgUser().username(FEDORA_USER)
                        .displayName(FEDORA_USER)
                        .role(Role.STANDARD)
                        .userType(UserType.PROGRAMMATIC_USER)
                        .enabledInOrg(true),
                new OrgUser().username(ADMIN_1_USER)
                        .displayName("Admin 1")
                        .role(Role.ADMIN)
                        .userType(UserType.GENERAL_USER)
                        .enabledInOrg(true),
                new OrgUser().username(USER_1_USER)
                        .displayName("User 1")
                        .role(Role.STANDARD)
                        .userType(UserType.GENERAL_USER)
                        .enabledInOrg(true)
        ));
    }

    @Test
    public void describeOrg() {
        var response = adminUserClient.describeOrg(TEST_ORG);

        assertEquals(TEST_ORG, response.getOrgName());
        assertEquals("Test Organization", response.getDisplayName());
        assertEquals(Role.ADMIN, response.getRole());
        assertThat(response.getAuthorities(), containsInAnyOrder(OrgAuthority.BASIC_OPS, OrgAuthority.ADMIN_OPS));
    }

    @Test
    public void createOrgAsServiceAdminUser() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com")
                .contactPhone("555-555-5555");
        serviceAdminUserClient.createOrg("new_org", orgRequest);

        var newOrg = serviceAdminUserClient.describeOrg("new_org");
        var contact = serviceAdminUserClient.describeOrgContact("new_org");
        assertEquals("organization display name", newOrg.getDisplayName());
        assertEquals("contact name", contact.getContactName());
        assertEquals("test@example.com", contact.getContactEmail());
        assertEquals("555-555-5555", contact.getContactPhone());
    }

    @Test
    public void failCreateOrgAsRegularUser() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com");

        assertThrows(AccessDeniedException.class, () -> {
            regularUserClient.createOrg("new_org", orgRequest);
        });
        assertThrows(NotFoundException.class, () -> {
            serviceAdminUserClient.describeOrg("new_org");
        });
    }

    @Test
    public void failCreateOrgAsAdminUser() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com");

        assertThrows(AccessDeniedException.class, () -> {
            adminUserClient.createOrg("new_org", orgRequest);
        });
        assertThrows(NotFoundException.class, () -> {
            serviceAdminUserClient.describeOrg("new_org");
        });
    }

    @Test
    public void failCreateShortOrg() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com");

        assertThat(assertThrows(ValidationException.class, () -> {
            serviceAdminUserClient.createOrg("new", orgRequest);
        }).getMessage(), containsString("4-64 characters long"));
    }

    @Test
    public void failCreateLongOrg() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com");

        assertThat(assertThrows(ValidationException.class, () -> {
            serviceAdminUserClient.createOrg("toolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongto", orgRequest);
        }).getMessage(), containsString("4-64 characters long"));
    }

    @Test
    public void failCreateOrgWithRestrictedChar() {
        var orgRequest = new CreateOrgRequest().displayName("organization display name")
                .contactName("contact name")
                .contactEmail("test@example.com");

        assertThat(assertThrows(ValidationException.class, () -> {
            serviceAdminUserClient.createOrg("new:org", orgRequest);
        }).getMessage(), containsString("Allowed special characters are: -_"));
    }
}
