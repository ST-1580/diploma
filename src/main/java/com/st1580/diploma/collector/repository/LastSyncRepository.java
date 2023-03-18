package com.st1580.diploma.collector.repository;

import com.st1580.diploma.scheduler.SchedulerType;

public interface LastSyncRepository {
    long getSchedulerLastSync(SchedulerType type);

    void setSchedulerLastSync(SchedulerType type, long lastSyncTsSec);
}
