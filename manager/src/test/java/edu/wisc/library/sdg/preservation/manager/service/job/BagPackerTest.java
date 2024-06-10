package edu.wisc.library.sdg.preservation.manager.service.job;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagPackerTest {

    private BagPacker packer;

    @BeforeEach
    public void setup() {
        packer = new BagPacker(1024);
    }

    @Test
    public void singleBagWithMultipleObjectsNotFull() {
        var o1v1 = versionDetails(10);
        var o1v2 = versionDetails(20);
        var o1v3 = versionDetails(30);

        var o2v1 = versionDetails(40);
        var o2v2 = versionDetails(50);

        var o3v1 = versionDetails(60);

        var full = packer.addObject(objectDetails(o1v1, o1v2, o1v3));
        assertEquals(0, full.size());

        full = packer.addObject(objectDetails(o2v1, o2v2));
        assertEquals(0, full.size());

        full = packer.addObject(objectDetails(o3v1));
        assertEquals(0, full.size());

        var bags = packer.getBags();

        assertEquals(1, bags.size());

        var contents = bags.stream().findFirst().get().getContents();

        assertThat(contents, containsInAnyOrder(o1v1, o1v2, o1v3, o2v1, o2v2, o3v1));
    }

    @Test
    public void multipleBagsNoneFull() {
        var o1v1 = versionDetails(300);
        var o1v2 = versionDetails(300);
        var o1v3 = versionDetails(300);

        var o2v1 = versionDetails(400);
        var o2v2 = versionDetails(400);

        var o3v1 = versionDetails(300);

        var full = packer.addObject(objectDetails(o1v1, o1v2, o1v3));
        assertEquals(0, full.size());

        full = packer.addObject(objectDetails(o2v1, o2v2));
        assertEquals(0, full.size());

        full = packer.addObject(objectDetails(o3v1));
        assertEquals(0, full.size());

        var bags = packer.getBags();

        assertEquals(3, bags.size());

        var bag1 = findBag(o1v1, bags);
        var bag2 = findBag(o2v1, bags);
        var bag3 = findBag(o3v1, bags);

        assertThat(bag1.getContents(), containsInAnyOrder(o1v1, o1v2, o1v3));
        assertThat(bag2.getContents(), containsInAnyOrder(o2v1, o2v2));
        assertThat(bag3.getContents(), containsInAnyOrder(o3v1));
    }

    @Test
    public void multipleObjectsWithVersionsSplitAcrossMultipleBags() {
        var o1v1 = versionDetails(500);
        var o1v2 = versionDetails(500);
        var o1v3 = versionDetails(200);
        var o1v4 = versionDetails(200);

        var o2v1 = versionDetails(100);
        var o2v2 = versionDetails(100);

        var o3v1 = versionDetails(500);

        var full = packer.addObject(objectDetails(o1v1, o1v2, o1v3, o1v4));
        assertEquals(1, full.size());

        assertThat(full.stream().findFirst().get().getContents(), containsInAnyOrder(o1v1, o1v2));

        full = packer.addObject(objectDetails(o2v1, o2v2));
        assertEquals(0, full.size());

        full = packer.addObject(objectDetails(o3v1));
        assertEquals(0, full.size());

        var bags = packer.getBags();

        assertEquals(2, bags.size());

        var bag1 = findBag(o1v3, bags);
        var bag2 = findBag(o3v1, bags);

        assertThat(bag1.getContents(), containsInAnyOrder(o1v3, o1v4, o2v1, o2v2));
        assertThat(bag2.getContents(), containsInAnyOrder(o3v1));
    }

    @Test
    public void objectVersionLargerThanMaxBagSize() {
        var o1v1 = versionDetails(2048);

        var full = packer.addObject(objectDetails(o1v1));
        assertEquals(1, full.size());
        assertThat(full.stream().findFirst().get().getContents(), containsInAnyOrder(o1v1));
    }

    private ObjectDetails objectDetails(ObjectVersionDetails... versionDetails) {
        var object = new ObjectDetails(UUID.randomUUID(), PreservationObjectState.ACTIVE);
        Arrays.asList(versionDetails).forEach(object::addVersion);
        return object;
    }

    private ObjectVersionDetails versionDetails(long size) {
        return new ObjectVersionDetails(1, size, 1);
    }

    private BagContents findBag(ObjectVersionDetails versionDetails, Collection<BagContents> bags) {
        for (var bag : bags) {
            for (var version : bag.getContents()) {
                if (version == versionDetails) {
                    return bag;
                }
            }
        }
        throw new RuntimeException("Expected a bag containing: " + versionDetails);
    }

}
