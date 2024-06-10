package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBagResponse;
import edu.wisc.library.sdg.preservation.manager.common.OperationContext;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.OrganizationService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingest")
@Timed(histogram = true)
public class IngestController {

    private static final Logger LOG = LoggerFactory.getLogger(IngestController.class);

    @Value("${app.upload.dir}")
    private Path uploadDir;

    private final PreservationAuth preservationAuth;
    private final IngestService ingestService;
    private final OrganizationService organizationService;
    private final JobBroker jobBroker;
    private final DistributionSummary bagSizeMetrics;

    @Autowired
    public IngestController(PreservationAuth preservationAuth,
                            IngestService ingestService,
                            OrganizationService organizationService,
                            JobBroker jobBroker,
                            MeterRegistry registry) {
        this.preservationAuth = preservationAuth;
        this.ingestService = ingestService;
        this.organizationService = organizationService;
        this.jobBroker = jobBroker;
        this.bagSizeMetrics = DistributionSummary.builder("sipSize")
                .baseUnit("bytes")
                .publishPercentileHistogram()
                .register(registry);
    }

    @PostMapping("/bag")
    public IngestBagResponse ingestBag(
            Authentication authentication,
            @RequestParam("vault") String vault,
            @RequestParam("file") MultipartFile[] files) {
        RequestFieldValidator.notBlank(vault, "vault");
        if (files == null || files.length != 1) {
            throw new ValidationException("Request must contain a single bag.");
        }

        var ns = organizationService.getVault(vault);

        return preservationAuth.apply(vault, Permission.WRITE, () -> {
            var context = OperationContext.create(authentication.getName(), ns.getOrgName(), vault);

            // TODO validate mime type?
            // TODO validate is zip?
            var file = files[0];
            var bagPath = writeUploadToDisk(file);

            var ingestId = ingestService.createIngestBatchForBag(context, bagPath, file.getOriginalFilename());

            jobBroker.checkPendingJobs();

            return new IngestBagResponse().ingestId(ingestId);
        });
    }

    private String writeUploadToDisk(MultipartFile file) {
        try {
            var path = uploadDir.resolve(UUID.randomUUID() + "-bag.zip");
            Files.createDirectories(path.getParent());
            LOG.info("Writing upload to {}", path);
            file.transferTo(path);

            bagSizeMetrics.record(Files.size(path));

            return path.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
