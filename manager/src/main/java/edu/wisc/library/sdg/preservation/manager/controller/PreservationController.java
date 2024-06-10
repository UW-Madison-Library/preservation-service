package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.DeleteObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.model.ObjectStorageProblem;
import edu.wisc.library.sdg.preservation.manager.client.model.PreservationObjectState;
import edu.wisc.library.sdg.preservation.manager.client.model.RemoteVersionCheck;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveEventsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectStorageProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/object")
@Timed(histogram = true)
public class PreservationController {

    private static final Logger LOG = LoggerFactory.getLogger(PreservationController.class);

    private final PreservationAuth preservationAuth;
    private final ModelMapper modelMapper;
    private final PreservationService preservationService;

    @Autowired
    public PreservationController(PreservationAuth preservationAuth,
                                  ModelMapper modelMapper,
                                  PreservationService preservationService) {
        this.preservationAuth = preservationAuth;
        this.modelMapper = modelMapper;
        this.preservationService = preservationService;
    }

    @GetMapping("")
    public DescribePreservationObjectResponse describePreservationObjectVersion(
            @RequestParam("vault") String vault,
            @RequestParam("externalObjectId") String externalObjectId,
            @RequestParam(value = "version", required = false) Integer version) {
        RequestFieldValidator.notBlank(vault, "vault");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        return preservationAuth.apply(vault, Permission.READ, () -> {
            var object = preservationService.getObject(vault, externalObjectId);
            var objectVersion = preservationService.getObjectVersionComposite(
                    vault, externalObjectId, version);
            var latestVersion = preservationService.getObjectVersion(object.getHeadObjectVersionId());

            // For these purposes we only care about the initial persistence version of the preservation version
            var remoteLocations = preservationService.getStorageLocations(
                    object.getObjectId(), objectVersion.getInitialPersistenceVersion());

            var locationsResponse = remoteLocations.stream()
                    .map(location -> {
                        return new RemoteVersionCheck()
                                .location(modelMapper.map(location.getDataStore(),
                                        edu.wisc.library.sdg.preservation.manager.client.model.DataStore.class))
                                .lastCheck(mapTimestamp(location.getLastCheckTimestamp()));
                    }).toList();

        return modelMapper.map(objectVersion, DescribePreservationObjectResponse.class)
                .state(PreservationObjectState.fromValue(object.getState().name()))
                .latestVersion(latestVersion.getVersion())
                .objectCreatedTimestamp(mapTimestamp(object.getCreatedTimestamp()))
                .remoteVersionCheck(locationsResponse)
                .versionCreatedTimestamp(mapTimestamp(objectVersion.getCreatedTimestamp()))
                .lastShallowCheck(mapTimestamp(object.getLastShallowCheckTimestamp()))
                .lastDeepCheck(mapTimestamp(object.getLastDeepCheckTimestamp()));
        });
    }

    @GetMapping("/event")
    public RetrieveEventsResponse retrieveObjectLogs(
            @RequestParam("vault") String vault,
            @RequestParam("externalObjectId") String externalObjectId) {
        RequestFieldValidator.notBlank(vault, "vault");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        return preservationAuth.apply(vault, Permission.READ, () -> {
            var events = preservationService.getObjectEvents(vault, externalObjectId);
            return new RetrieveEventsResponse().events(modelMapper.mapList(events, Event.class));
        });
    }

    @PostMapping("/delete")
    public void deleteObject(Authentication authentication, @RequestBody DeleteObjectRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notBlank(request.getReason(), "reason");

        preservationAuth.applyIfServiceAdmin(() -> {
            preservationService.markObjectAsDeleted(
                    request.getVault(),
                    request.getExternalObjectId(),
                    request.getReason(),
                    authentication.getName());
        });
    }

    @GetMapping("problems/storage")
    public RetrieveObjectStorageProblemsResponse retrieveObjectStorageProblems(
            @RequestParam("vault") String vault,
            @RequestParam("externalObjectId") String externalObjectId) {
        RequestFieldValidator.notBlank(vault, "vault");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        return preservationAuth.apply(vault, Permission.READ, () -> {
            var objectId = preservationService.getInternalObjectId(vault, externalObjectId);
            var problems = preservationService.getObjectStorageProblems(objectId);
            return new RetrieveObjectStorageProblemsResponse()
                    .problems(modelMapper.mapList(problems, ObjectStorageProblem.class));
        });
    }

    private OffsetDateTime mapTimestamp(LocalDateTime original) {
        if (original == null) {
            return null;
        }
        return original.atOffset(ZoneOffset.UTC);
    }

}
