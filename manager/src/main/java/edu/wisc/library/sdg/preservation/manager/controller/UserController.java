package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.AuthUtil;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeUserResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListUserOrgsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.Role;
import edu.wisc.library.sdg.preservation.manager.client.model.UpdateUserVaultPermissionsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.User;
import edu.wisc.library.sdg.preservation.manager.client.model.UserOrganization;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.service.OrganizationService;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Timed(histogram = true)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final PreservationAuth preservationAuth;
    private final OrganizationService organizationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(PreservationAuth preservationAuth,
                          OrganizationService organizationService,
                          UserService userService,
                          ModelMapper modelMapper) {
        this.preservationAuth = preservationAuth;
        this.organizationService = organizationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public DescribeUserResponse describeUser(Authentication authentication) {
        var user = userService.getByUsername(authentication.getName());
        return new DescribeUserResponse().user(modelMapper.map(user, User.class));
    }

    @GetMapping("/org")
    public ListUserOrgsResponse listUserOrgs(Authentication authentication) {
        List<UserOrganization> responseOrgs;

        if (AuthUtil.isServiceAdmin(authentication)) {
            var orgs = organizationService.getAllOrganizations();
            responseOrgs = modelMapper.mapList(orgs, UserOrganization.class);

            var authorities = List.of(
                    edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority.BASIC_OPS,
                    edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority.ADMIN_OPS
            );

            responseOrgs.forEach(org -> {
                org.role(Role.ADMIN).authorities(authorities);
            });
        } else {
            var orgs = organizationService.getUserOrgs(authentication.getName());
            responseOrgs = modelMapper.mapList(orgs, UserOrganization.class);

            responseOrgs.forEach(org -> {
                var userOrgOpt = userService.getOrgUser(authentication.getName(), org.getOrgName());

                if (userOrgOpt.isPresent()) {
                    var userOrgMap = userOrgOpt.get();
                    if (userOrgMap.isEnabled()) {
                        var authorities = new ArrayList<>(userOrgMap.getRole().getAuthorities());
                        org.setAuthorities(modelMapper.mapList(authorities, edu.wisc.library.sdg.preservation.manager.client.model.OrgAuthority.class));
                        org.setRole(modelMapper.map(userOrgMap.getRole(), Role.class));
                    }
                }
            });
        }

        return new ListUserOrgsResponse().orgs(responseOrgs);
    }

    @PostMapping("/vault")
    public void updateUserVaultPermissions(@RequestBody UpdateUserVaultPermissionsRequest request) {
        RequestFieldValidator.notBlank(request.getUsername(), "username");
        RequestFieldValidator.notNull(request.getVaultPermissions(), "vaultPermissions");
        RequestFieldValidator.notBlank(request.getVaultPermissions().getVault(), "vault");

        var vault = request.getVaultPermissions().getVault();
        var ns = organizationService.getVault(vault);

        preservationAuth.apply(ns.getOrgName(), OrgAuthority.ADMIN_OPS, () -> {
            Set<Permission> perms;
            if (request.getVaultPermissions().getPermissions() == null) {
                perms = Collections.emptySet();
            } else {
                perms = new HashSet<>(modelMapper.mapList(request.getVaultPermissions().getPermissions(), Permission.class));
            }

            userService.setVaultPermissions(request.getUsername(), vault, perms);
        });
    }

}