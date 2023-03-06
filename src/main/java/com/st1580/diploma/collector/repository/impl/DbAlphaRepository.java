package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.db.tables.records.AlphaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;

@Repository
public class DbAlphaRepository implements AlphaRepository {

    @Autowired
    private DSLContext context;
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;

    @Override
    public Map<Long, AlphaEntity> collectAllEntitiesByIds(Collection<Long> ids) {
        return context
                .selectFrom(ALPHA)
                .where(ALPHA.ID.in(ids))
                .fetch()
                .stream()
                .map(this::convertToAlphaEntity)
                .collect(Collectors.toMap(
                        AlphaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, alphaToBetaRepository.getConnectedBetaEntitiesIdsByAlphaIds(ids));
            res.put(EntityType.GAMMA, gammaToAlphaRepository.getConnectedGammaEntitiesIdsByAlphaIds(ids));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, new HashMap<>(alphaToBetaRepository.getConnectedBetaEntitiesByAlphaIds(ids)));
            res.put(EntityType.GAMMA, new HashMap<>(gammaToAlphaRepository.getConnectedGammaEntitiesByAlphaIds(ids)));
        });

        return res;
    }

    @Override
    public void insert(AlphaEntity entity) {
        context.insertInto(ALPHA).set(convertToAlphaRecord(entity)).execute();
    }

    @Override
    public void update(long id, AlphaEntity entity) {
        context.update(ALPHA).set(convertToAlphaRecord(entity)).where(ALPHA.ID.eq(id)).execute();
    }

    @Override
    public void delete(long id) {
        context.transaction(ctx -> {
            context.deleteFrom(ALPHA_TO_BETA).where(ALPHA_TO_BETA.ALPHA_ID.eq(id)).execute();
            context.deleteFrom(ALPHA).where(ALPHA.ID.eq(id)).execute();
        });
    }

    @Override
    public List<Long> getAllIds() {
        return context
                .select(ALPHA.ID)
                .from(ALPHA)
                .fetchInto(Long.class);
    }

    private AlphaRecord convertToAlphaRecord(AlphaEntity entity) {
        return new AlphaRecord(
                entity.getId(),
                entity.getProperty_1(),
                entity.getProperty_2(),
                entity.getProperty_3()
        );
    }

    private AlphaEntity convertToAlphaEntity(AlphaRecord record) {
        return new AlphaEntity(
                record.getId(),
                record.getProperty_1(),
                record.getProperty_2(),
                record.getProperty_3()
        );
    }


}
