package com.st1580.diploma.repository;

import com.st1580.diploma.scheduler.SchedulerType;

public interface LastSyncRepository {
    long getCorrectorLastSync();

    long getServicesUpdateTs();

    long getSchedulerLastSync(SchedulerType type);

    void setSchedulerLastSync(SchedulerType type, long lastSyncTsSec);
}
