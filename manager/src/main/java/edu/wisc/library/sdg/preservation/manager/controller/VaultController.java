package edu.wisc.library.sdg.preservation.manager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateVaultRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeRetrieveObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeVaultResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultPermissionsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultUsersResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveRequestPart;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultObject;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermission;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultPermissionsWithDescription;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultUser;
import edu.wisc.library.sdg.preservation.manager.common.OperationContext;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.model.Vault;
import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.OrganizationService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vault")
@Timed(histogram = true)
public class VaultController {

    private static final Logger LOG = LoggerFactory.getLogger(VaultController.class);

    private static final Pattern VAULT_PATTERN = Pattern.compile("^[a-zA-Z0-9-_ ]{4,128}$");

    private final Path disseminationDir;
    private final PreservationAuth preservationAuth;
    private final OrganizationService organizationService;
    private final UserService userService;
    private final PreservationService preservationService;
    private final JobService jobService;
    private final JobBroker jobBroker;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public VaultController(@Value("${app.dissemination.dir}") Path disseminationDir,
                           PreservationAuth preservationAuth,
                           OrganizationService organizationService,
                           UserService userService,
                           PreservationService preservationService,
                           JobService jobService,
                           JobBroker jobBroker,
                           ModelMapper modelMapper,
                           ObjectMapper objectMapper) {
        this.disseminationDir = disseminationDir;
        this.preservationAuth = preservationAuth;
        this.organizationService = organizationService;
        this.userService = userService;
        this.preservationService = preservationService;
        this.jobService = jobService;
        this.jobBroker = jobBroker;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{orgName}")
    public ListVaultsResponse listVaults(@PathVariable("orgName") String orgName) {
        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var vaults = organizationService.getVaults(orgName);
            var response = new ListVaultsResponse();

            response.setVaults(modelMapper.mapList(vaults, VaultObject.class));

            return response;
        });
    }

    @GetMapping("/{orgName}/permission")
    public ListVaultPermissionsResponse listOrgUserVaultPermissions(
            Authentication authentication,
            @PathVariable("orgName") String orgName,
            @RequestParam(value = "username", required = false) String username) {
        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var vaultMap = organizationService.getVaults(orgName).stream()
                    .collect(Collectors.toMap(Vault::getName, Function.identity()));

            var name = username == null ? authentication.getName() : username;
            var permissions = userService.listUserOrgVaultPermissions(name, orgName);

            var responsePerms = new HashMap<String, VaultPermissionsWithDescription>();

            for (var perm : permissions) {
                var description = vaultMap.get(perm.getVault()).getDescription();
                var responsePerm = responsePerms
                        .computeIfAbsent(perm.getVault(), k -> new VaultPermissionsWithDescription()
                                .vault(new VaultObject().name(k).description(description)));
                responsePerm.addPermissionsItem(VaultPermission.fromValue(perm.getPermission().toString()));
                // This is just done to make testing easier...
                responsePerm.getPermissions().sort(Comparator.naturalOrder());
            }

            vaultMap.entrySet().stream()
                    .filter(entry -> !responsePerms.containsKey(entry.getKey()))
                    .forEach(entry -> {
                        responsePerms.put(entry.getKey(), new VaultPermissionsWithDescription()
                                .vault(new VaultObject().name(entry.getKey()).description(entry.getValue().getDescription()))
                                .permissions(Collections.emptyList()));
            });

            return new ListVaultPermissionsResponse()
                    .vaultPermissions(new ArrayList<>(responsePerms.values()));
        });
    }

    @GetMapping("/{vault}/user")
    public ListVaultUsersResponse listUsersInVault(@PathVariable("vault") String vault) {
        var orgName = organizationService.getVault(vault).getOrgName();

        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var users = organizationService.getUsersWithVaultAccess(vault);
            return new ListVaultUsersResponse().users(modelMapper.mapList(users, VaultUser.class));
        });
    }

    @GetMapping("/{vault}/describe")
    public DescribeVaultResponse describeVault(Authentication authentication,
                                               @PathVariable("vault") String vaultName) {
        var vault = organizationService.getVault(vaultName);

        return preservationAuth.apply(vault.getOrgName(), OrgAuthority.BASIC_OPS, () -> {
            var permissions = userService.listUserVaultPermissions(authentication.getName(), vaultName).stream()
                    .map(edu.wisc.library.sdg.preservation.manager.db.model.VaultPermission::getPermission).collect(Collectors.toList());

            return new DescribeVaultResponse()
                    .orgName(vault.getOrgName())
                    .vault(new VaultObject()
                            .name(vaultName)
                            .description(vault.getDescription()))
                    .objectCount(vault.getObjects())
                    .approximateStorageMegabytes(vault.getStorageMb())
                    .permissions(modelMapper.mapList(permissions, VaultPermission.class));
        });
    }

    @GetMapping("/{vault}/problems")
    public ListVaultProblemsResponse listVaultProblems(@PathVariable("vault") String vaultName) {
        var vault = organizationService.getVault(vaultName);

        return preservationAuth.apply(vault.getOrgName(), OrgAuthority.BASIC_OPS, () -> {
            var ids = preservationService.getExternalObjectIdsWithStorageProblems(vaultName);
            return new ListVaultProblemsResponse().objects(ids);
        });
    }

    @PostMapping
    public void createVault(Authentication authentication,
                            @RequestBody CreateVaultRequest request) {
        var message = "be 4-128 characters long and may include spaces, lowercase and uppercase ASCII characters, and numbers. Allowed special characters are: -_";

        RequestFieldValidator.matchPattern(request.getVault().getName(), VAULT_PATTERN, "vault", message);

        preservationAuth.apply(request.getOrgName(), OrgAuthority.ADMIN_OPS, () -> {
            organizationService.createVault(request.getVault().getName(),
                    request.getVault().getDescription(), request.getOrgName());

            // The user who created the vault should have access to it
            userService.setVaultPermissions(
                    authentication.getName(),
                    request.getVault().getName(),
                    Set.of(Permission.READ, Permission.WRITE));
        });
    }

    @PostMapping("/retrieve")
    public RetrieveObjectsResponse retrieveObjects(Authentication authentication,
                                                   @RequestBody RetrieveObjectsRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");

        var vault = organizationService.getVault(request.getVault());

        return preservationAuth.applyIfServiceAdmin(() -> {
            var context = OperationContext.create(authentication.getName(), vault.getOrgName(), request.getVault());
            var requestId = preservationService.retrieveObjects(context,
                    request.getExternalObjectIds(),
                    Optional.ofNullable(request.getAllVersions()).orElse(false));
            jobBroker.checkPendingJobs();
            return new RetrieveObjectsResponse().requestId(requestId);
        });
    }

    @GetMapping(value = "/retrieve/{requestId}")
    public DescribeRetrieveObjectsResponse describeRetrieveRequest(@PathVariable("requestId") Long requestId) {

        return preservationAuth.applyIfServiceAdmin(() -> {
            var request = preservationService.getRetrieveRequest(requestId);
            var jobs = preservationService.getRetrieveRequestJobs(requestId);

            var response = new DescribeRetrieveObjectsResponse()
                    .username(request.getUsername())
                    .vault(request.getVault())
                    .allVersions(request.isAllVersions())
                    .deleted(request.isDeleted())
                    .createdTimestamp(Time.utc(request.getCreatedTimestamp()))
                    .deletedTimestamp(Time.utc(request.getDeletedTimestamp()));

            if (StringUtils.isNotEmpty(request.getExternalObjectIds())) {
                response.setExternalObjectIds(objectMapper.readValue(request.getExternalObjectIds(), new TypeReference<>() { }));
            }

            var isDeleted = request.isDeleted();

            jobs.forEach(job -> {
                var state = RetrieveRequestPart.StateEnum.NOT_READY;

                if (isDeleted) {
                    state = RetrieveRequestPart.StateEnum.DELETED;
                } else if (job.getState() == JobState.COMPLETE) {
                    state = RetrieveRequestPart.StateEnum.READY;
                } else if (job.getState() == JobState.FAILED) {
                    state = RetrieveRequestPart.StateEnum.FAILED;
                } else if (job.getState() == JobState.CANCELLED) {
                    state = RetrieveRequestPart.StateEnum.CANCELLED;
                }

                response.addPartsItem(new RetrieveRequestPart()
                        .jobId(job.getJobId())
                        .state(state)
                        .lastDownloadedTimestamp(Time.utc(job.getLastDownloadedTimestamp())));
            });

            return response;
        });
    }

    @PostMapping(value = "/retrieve/{requestId}/retry/{jobId}")
    public void retryRetrieveJob(@PathVariable("requestId") Long requestId,
                                 @PathVariable("jobId") Long jobId) {
        preservationAuth.applyIfServiceAdmin(() -> {
            var request = preservationService.getRetrieveRequest(requestId);

            if (request.isDeleted()) {
                throw new SafeRuntimeException("Cannot retry job because the request has expired.");
            }

            var job = preservationService.getRetrieveRequestJob(jobId);

            if (!Objects.equals(job.getRetrieveRequestId(), requestId)) {
                throw new ValidationException(String.format("Job %s is not part of retrieve request %s", jobId, requestId));
            }

            jobService.updateJobState(jobId, JobState.PENDING);
            jobBroker.checkPendingJobs();
        });
    }

    @GetMapping(value = "/retrieve/download/{jobId}", produces = { "application/zip" })
    public ResponseEntity<InputStreamResource> downloadDip(@PathVariable("jobId") Long jobId) {
        var job = jobService.retrieveJob(jobId);

        return preservationAuth.apply(job.getOrgName(), OrgAuthority.ADMIN_OPS, () -> {
            if (job.getType() != JobType.RETRIEVE_OBJECTS) {
                throw new ValidationException(String.format("Job %s is not a retrieve object job", jobId));
            }
            if (job.getState() != JobState.COMPLETE) {
                throw new ValidationException(String.format("Job %s cannot be downloaded because its state is %s",
                        jobId, job.getState()));
            }
            var file = disseminationDir.resolve(String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId));

            if (Files.notExists(file)) {
                throw new NotFoundException("Cannot download because file does not exist");
            }

            preservationService.markRetrieveJobDownloaded(jobId);

            try {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + file.getFileName().toString())
                        .contentType(new MediaType("application", "zip"))
                        .contentLength(Files.size(file))
                        .body(new InputStreamResource(Files.newInputStream(file)));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

}