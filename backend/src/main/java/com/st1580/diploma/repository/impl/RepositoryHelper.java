package com.st1580.diploma.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.st1580.diploma.repository.types.EntityActiveType;

public class RepositoryHelper {
    /**
     *
     * @return list of batches. Every element in batch has unique entityId
     */
    public static <T> List<Set<T>> getBatches(Map<Long, List<T>> listByEntityId) {
        List<Set<T>> batches = new ArrayList<>();
        int currPos = 0;
        while (!listByEntityId.isEmpty()) {
            Set<T> currBatch = new HashSet<>();
            List<Long> idsToRemove = new ArrayList<>();

            for (long id :  listByEntityId.keySet()) {
                List<T> events = listByEntityId.get(id);
                currBatch.add(events.get(currPos));

                if (events.size() == currPos + 1) {
                    idsToRemove.add(id);
                }
            }

            for (long id : idsToRemove) {
                listByEntityId.remove(id);
            }

            batches.add(currBatch);
            currPos++;
        }

        return batches;
    }

    public static Map<Long, List<Long>> fillConnectedEntities(Collection<Long> ids,
                                                               Map<Long, List<Long>> existingLinks) {
        Map<Long, List<Long>> connectedEntities = new HashMap<>();
        for (long entityId : ids) {
            connectedEntities.put(
                    entityId,
                    existingLinks.containsKey(entityId) ? existingLinks.get(entityId) : new ArrayList<>()
            );
        }

        return connectedEntities;
    }

    public static boolean isActiveLinkByEndStatus(EntityActiveType type, boolean currentActivity) {
        return type == EntityActiveType.DELETED ? false : currentActivity;
    }
}
