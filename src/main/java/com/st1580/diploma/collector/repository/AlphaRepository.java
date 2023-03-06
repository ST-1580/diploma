package com.st1580.diploma.collector.repository;


import java.util.List;

import com.st1580.diploma.collector.graph.entities.AlphaEntity;

public interface AlphaRepository extends CollectorRepository {
    void insert(AlphaEntity entity);

    void update(long id, AlphaEntity entity);

    void delete(long id);

    List<Long> getAllIds();
}
