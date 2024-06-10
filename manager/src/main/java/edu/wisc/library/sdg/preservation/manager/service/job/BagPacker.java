package edu.wisc.library.sdg.preservation.manager.service.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Packs object versions into bags with the goal of packing all of an object's versions into the same bag if at all possible.
 * Only if all of the versions are too big to fit in a single bag are the versions split across bags. In this case,
 * versions are never split and as many versions as possible are stored within the same bag.
 *
 * If a bag as room, it may contain multiple objects. There is no relationship between objects in bags.
 */
public class BagPacker {

    private final long maxSizeBytes;
    private final Set<BagContents> bags;

    public BagPacker(long maxSizeBytes) {
        this.maxSizeBytes = maxSizeBytes;
        bags = new HashSet<>();
    }

    public Collection<BagContents> addObject(ObjectDetails object) {
        var fullBags = new HashSet<BagContents>();

        if (object.size() > maxSizeBytes && object.getVersions().size() > 1) {
            var splits = split(object);
            for (var split : splits) {
                fullBags.addAll(addObject(split));
            }
        } else {
            var bag = add(object);
            if (bag.isFull()) {
                fullBags.add(bag);
            }
        }

        bags.removeAll(fullBags);

        return fullBags;
    }

    public Collection<BagContents> getBags() {
        return bags;
    }

    public boolean hasBags() {
        return !bags.isEmpty();
    }

    private List<ObjectDetails> split(ObjectDetails object) {
        var splits = new ArrayList<ObjectDetails>();
        var current = new ObjectDetails(object.getObjectId(), object.getState());

        for (var version : object.getVersions()) {
            if (current.size() + version.size() > maxSizeBytes) {
                splits.add(current);
                current = new ObjectDetails(object.getObjectId(), object.getState());
            }
            current.addVersion(version);
        }

        if (!current.getVersions().isEmpty()) {
            splits.add(current);
        }

        return splits;
    }

    private BagContents add(ObjectDetails object) {
        BagContents bag = null;
        var added = false;

        for (var it = bags.iterator(); it.hasNext();) {
            bag = it.next();
            added = bag.add(object);
            if (added) {
                break;
            }
        }
        if (!added) {
            bag = new BagContents(maxSizeBytes);
            bags.add(bag);
            bag.forceAdd(object);
        }

        return bag;
    }

}
