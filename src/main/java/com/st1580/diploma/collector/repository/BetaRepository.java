package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.entities.BetaEntity;

public interface BetaRepository extends CollectorRepository {
    void insert(BetaEntity entity);

    void update(long id, BetaEntity entity);

    void delete(long id);

    List<Long> getAllIds();
}
