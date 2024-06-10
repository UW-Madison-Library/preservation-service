package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.worker.storage.ocfl.RawOcflStore;
import io.ocfl.api.OcflRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FinalizeRestoreProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeRestoreProcessor.class);

    private final OcflRepository ocflRepo;
    private final RawOcflStore rawOcflStore;

    public FinalizeRestoreProcessor(@Qualifier("localOcflRepo") OcflRepository ocflRepo,
                                    RawOcflStore rawOcflStore) {
        this.ocflRepo = ocflRepo;
        this.rawOcflStore = rawOcflStore;
    }

    public void finalizeRestore(String objectId) {
        ArgCheck.notBlank(objectId, "objectId");

        LOG.info("Finalizing restore of object <{}>", objectId);

        rawOcflStore.installRootInventory(objectId);
        ocflRepo.invalidateCache(objectId);
    }

}
