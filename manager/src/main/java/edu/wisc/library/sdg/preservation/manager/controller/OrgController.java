package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.AuthUtil;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateOrgRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeOrgResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListOrgUsersResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.OrgUser;
import edu.wisc.library.sdg.preservation.manager.client.model.OrganizationContactResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.Role;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgRole;
import edu.wisc.library.sdg.preservation.manager.service.OrganizationService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/org")
@Timed(histogram = true)
public class OrgController {

    private static final Logger LOG = LoggerFactory.getLogger(OrgController.class);
    private static final Pattern ORG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_ ]{4,64}$");

    private final PreservationAuth preservationAuth;
    private final OrganizationService organizationService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrgController(PreservationAuth preservationAuth,
                         OrganizationService organizationService,
                         ModelMapper modelMapper) {
        this.preservationAuth = preservationAuth;
        this.organizationService = organizationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{orgName}/user")
    public ListOrgUsersResponse listOrgUsers(@PathVariable("orgName") String orgName) {
        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var users = organizationService.getUsersInOrg(orgName);
            return new ListOrgUsersResponse().users(modelMapper.mapList(users, OrgUser.class));
        });
    }

    @GetMapping("/{orgName}")
    public DescribeOrgResponse describeOrg(Authentication authentication,
                                           @PathVariable("orgName") String orgName) {
        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var org = organizationService.getOrganization(orgName);

            OrgRole role;

            if (AuthUtil.isServiceAdmin(authentication)) {
                role = OrgRole.ADMIN;
            } else {
                var orgUser = organizationService.getOrgUser(orgName, authentication.getName());
                role = orgUser.getRole();
            }

            var authorities = new ArrayList<>(role.getAuthorities());

            return new DescribeOrgResponse()
                    .orgName(orgName)
                    .displayName(org.getDisplayName())
                    .role(modelMapper.map(role, Role.class))
                    .authorities(modelMapper.mapList(authorities, edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority.class));
        });
    }

    @PostMapping("/{orgName}")
    public void createOrg(@PathVariable("orgName") String orgName,
                          @RequestBody CreateOrgRequest request) {
        preservationAuth.applyIfServiceAdmin(() -> {
            var message = "be 4-64 characters long and may include spaces, lowercase and uppercase ASCII characters, and numbers. Allowed special characters are: -_";

            RequestFieldValidator.matchPattern(orgName, ORG_NAME_PATTERN, "orgName", message);

            organizationService.createOrganization(orgName,
                    RequestFieldValidator.notBlank(request.getDisplayName(), "displayName"),
                    RequestFieldValidator.notBlank(request.getContactName(), "contactName"),
                    RequestFieldValidator.notBlank(request.getContactEmail(), "contactEmail"),
                    RequestFieldValidator.notBlank(request.getContactPhone(), "contactPhone"));
        });
    }

    @GetMapping("/{orgName}/contact")
    public OrganizationContactResponse describeOrgContact(@PathVariable("orgName") String orgName) {
        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var org = organizationService.getOrganization(orgName);

            return new OrganizationContactResponse()
                    .contactName(org.getContactName())
                    .contactEmail(org.getContactEmail())
                    .contactPhone(org.getContactPhone());
        });
    }

}