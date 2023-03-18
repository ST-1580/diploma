package com.st1580.diploma.collector.repository.impl;

import java.util.Objects;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.LastSyncRepository;
import com.st1580.diploma.scheduler.SchedulerType;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.LAST_SYNC;

@Repository
public class DbLastSyncRepository implements LastSyncRepository {
    @Inject
    private DSLContext context;

    @Override
    public long getSchedulerLastSync(SchedulerType type) {
        return Objects.requireNonNull(context
                        .select(LAST_SYNC.LAST_SYNC_TS)
                        .from(LAST_SYNC)
                        .where(LAST_SYNC.NAME.eq(type.name()))
                        .fetchOne())
                .get(0, Long.class);
    }

    @Override
    public void setSchedulerLastSync(SchedulerType type, long lastSyncTsSec) {
        context.update(LAST_SYNC)
                .set(LAST_SYNC.LAST_SYNC_TS, lastSyncTsSec)
                .where(LAST_SYNC.NAME.eq(type.name()))
                .execute();
    }
}
