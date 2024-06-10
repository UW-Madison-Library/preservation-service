package edu.wisc.library.sdg.preservation.manager;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelMappingTest {

    private Mapper dozerMapper;

    @BeforeEach
    public void setup() {
        dozerMapper = DozerBeanMapperBuilder.create()
                .withMappingFiles("mapping.xml")
                .build();
    }

    @Test
    public void ingestBatchDbToInternalDto() {
        var original = new IngestBatch()
                .setIngestId(1L)
                .setOrgName("orgName")
                .setVault("vault")
                .setCreatedBy("createdBy")
                .setReviewedBy("approvedBy")
                .setState(IngestBatchState.RECEIVED)
                .setFilePath("filePath")
                .setReceivedTimestamp(LocalDateTime.now())
                .setUpdatedTimestamp(LocalDateTime.now());

        var mapped = dozerMapper.map(original, edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch.class);

        assertEquals(original.getIngestId(), mapped.getIngestId());
        assertEquals(original.getOrgName(), mapped.getOrgName());
        assertEquals(original.getVault(), mapped.getVault());
        assertEquals(original.getCreatedBy(), mapped.getCreatedBy());
        assertEquals(original.getReviewedBy(), mapped.getReviewedBy());
        assertEquals(original.getState().toString(), mapped.getState().toString());
        assertEquals(original.getFilePath(), mapped.getFilePath());
        assertEquals(original.getReceivedTimestamp().toInstant(ZoneOffset.UTC), mapped.getReceivedTimestamp().toInstant());
        assertEquals(original.getUpdatedTimestamp().toInstant(ZoneOffset.UTC), mapped.getUpdatedTimestamp().toInstant());
    }

    @Test
    public void ingestBatchObjectToDto() {
        var original = new IngestBatchObject()
                .setIngestId(1L)
                .setIngestObjectId(1L)
                .setExternalObjectId("extId")
                .setState(IngestObjectState.ANALYZING)
                .setObjectRootPath("path")
                .setHeadObjectVersionId(1L);

        var mapped = dozerMapper.map(original, edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject.class);

        assertEquals(original.getIngestId(), mapped.getIngestId());
        assertEquals(original.getExternalObjectId(), mapped.getExternalObjectId());
        assertEquals(original.getState().toString(), mapped.getState().toString());
    }

}
