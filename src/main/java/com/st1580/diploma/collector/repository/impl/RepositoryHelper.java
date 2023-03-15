package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepositoryHelper {
    public static <T> List<List<T>> getBatches(Map<Long, List<T>> listByEntityId) {
        List<List<T>> batches = new ArrayList<>();
        int currPos = 0;
        while (!listByEntityId.isEmpty()) {
            List<T> currBatch = new ArrayList<>();
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
}
