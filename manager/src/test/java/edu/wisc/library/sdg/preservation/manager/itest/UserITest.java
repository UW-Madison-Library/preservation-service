package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.client.model.Role;
import edu.wisc.library.sdg.preservation.manager.client.model.UserOrganization;
import edu.wisc.library.sdg.preservation.manager.client.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserITest extends ITestBase {

    @Test
    public void listOrgs() {
        List<UserOrganization> orgs = regularUserClient.listUserOrgs().getOrgs();

        assertEquals(1, orgs.size());

        var org = orgs.get(0);
        assertEquals(TEST_ORG, org.getOrgName());
        assertEquals("Test Organization", org.getDisplayName());
        assertThat(org.getAuthorities(), containsInAnyOrder(OrgAuthority.BASIC_OPS));
        assertEquals(Role.STANDARD, org.getRole());
    }

    @Test
    public void listOrgsAdmin() {
        List<UserOrganization> orgs = adminUserClient.listUserOrgs().getOrgs();

        assertEquals(1, orgs.size());

        var org = orgs.get(0);
        assertEquals(TEST_ORG, org.getOrgName());
        assertEquals("Test Organization", org.getDisplayName());
        assertThat(org.getAuthorities(), containsInAnyOrder(OrgAuthority.BASIC_OPS, OrgAuthority.ADMIN_OPS));
        assertEquals(Role.ADMIN, org.getRole());
    }

    @Test
    public void listOrgsServiceAdmin() {
        var orgs = serviceAdminUserClient.listUserOrgs().getOrgs();
        var names = orgs.stream().map(UserOrganization::getOrgName).toList();
        var roles = orgs.stream().map(UserOrganization::getRole).toList();

        assertThat(names, containsInAnyOrder(TEST_ORG, ANOTHER_ORG));
        assertThat(roles, containsInAnyOrder(Role.ADMIN, Role.ADMIN));
    }

    @Test
    public void describeUser() {
        var user = adminUserClient.describeUser().getUser();

        assertEquals(ADMIN_1_USER, user.getUsername());
        assertEquals("Admin 1", user.getDisplayName());
        assertEquals(UserType.GENERAL_USER, user.getUserType());
        assertTrue(user.getEnabled());
    }

    @Test
    public void describeUserServiceAdmin() {
        var user = serviceAdminUserClient.describeUser().getUser();

        assertEquals(SERVICE_ADMIN_1_USER, user.getUsername());
        assertEquals(UserType.SERVICE_ADMIN, user.getUserType());
        assertTrue(user.getEnabled());
    }

}
