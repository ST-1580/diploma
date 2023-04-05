package com.st1580.diploma.collector.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BfsStage {
    private Map<EntityType, Set<Long>> oddStage;
    private Map<EntityType, Set<Long>> evenStage;
    private long currStageLvl;

    public BfsStage(EntityType type, long entityId) {
        this.oddStage = new HashMap<>();
        oddStage.put(type, Set.of(entityId));
        this.evenStage = new HashMap<>();
        this.currStageLvl = 1;
    }

    private Map<EntityType, Set<Long>> getCurrLvlStage() {
        return currStageLvl % 2 == 0 ? evenStage : oddStage;
    }

    public Set<Long> getCurrentStageIdsWithType(EntityType type) {
        return Objects.requireNonNullElse(getCurrLvlStage().get(type), new HashSet<>());
    }

    public void putInNextLvlByType(EntityType type, Set<Long> ids) {
        if (currStageLvl % 2 == 0) {
            if (oddStage.containsKey(type)) {
                Set<Long> was = oddStage.get(type);
                was.addAll(ids);
                oddStage.put(type, was);
            } else {
                oddStage.put(type, ids);
            }
        } else {
            if (evenStage.containsKey(type)) {
                Set<Long> was = evenStage.get(type);
                was.addAll(ids);
                evenStage.put(type, was);
            } else {
                evenStage.put(type, ids);
            }
        }
    }

    public void incLvl() {
        if (currStageLvl % 2 == 0) {
            evenStage = new HashMap<>();
        } else {
            oddStage = new HashMap<>();
        }
        currStageLvl++;
    }

    public boolean hasNodesInCurrentLvl() {
        Map<EntityType, Set<Long>> currStage = getCurrLvlStage();
        long totalSize = 0;
        for (EntityType type : currStage.keySet()) {
            totalSize += currStage.get(type).size();
        }

        return totalSize != 0;
    }
}
