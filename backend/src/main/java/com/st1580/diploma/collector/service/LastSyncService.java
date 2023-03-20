package com.st1580.diploma.collector.service;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.LastSyncController;
import com.st1580.diploma.repository.LastSyncRepository;
import com.st1580.diploma.scheduler.SchedulerType;
import org.springframework.stereotype.Service;

@Service
public class LastSyncService implements LastSyncController {
    @Inject
    private LastSyncRepository lastSyncRepository;

    @Override
    public long getServiceLastSync(SchedulerType type) {
        return lastSyncRepository.getSchedulerLastSync(type);
    }
}
