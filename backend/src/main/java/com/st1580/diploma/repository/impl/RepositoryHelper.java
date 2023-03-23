package com.st1580.diploma.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    public static <T> Map<Long, List<T>> fillConnectedEntities(Collection<Long> ids,
                                                               Map<Long, List<T>> existingLinks) {
        Map<Long, List<T>> connectedEntities = new HashMap<>();
        for (long entityId : ids) {
            connectedEntities.put(
                    entityId,
                    existingLinks.containsKey(entityId) ? existingLinks.get(entityId) : new ArrayList<>()
            );
        }

        return connectedEntities;
    }
}
