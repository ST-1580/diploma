package com.st1580.diploma.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.st1580.diploma.repository.LastSyncRepository;
import com.st1580.diploma.updater.service.BetaUpdateService;
import com.st1580.diploma.updater.service.DeltaUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class DeltaScheduler {
    private final static SchedulerType schedulerType = SchedulerType.DELTA;
    private final ScheduledThreadPoolExecutor executor;
    private final DeltaUpdateService deltaUpdateService;
    private final LastSyncRepository lastSyncRepository;
    private final long rangeCrossingMillis;
    private final long updateRangeMillis;
    private final long updateDelaySeconds;

    @Inject
    public DeltaScheduler(DeltaUpdateService deltaUpdateService,
                         LastSyncRepository lastSyncRepository,
                         @Value("${scheduler.delta.range.crossing.seconds}") long rangeCrossingSeconds,
                         @Value("${scheduler.delta.update.range.minutes}") long updateRangeMinutes,
                         @Value("${scheduler.delta.update.delay.seconds}") long updateDelaySeconds) {
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.deltaUpdateService = deltaUpdateService;
        this.lastSyncRepository = lastSyncRepository;
        this.rangeCrossingMillis = TimeUnit.SECONDS.toMillis(rangeCrossingSeconds);
        this.updateRangeMillis = TimeUnit.MINUTES.toMillis(updateRangeMinutes);
        this.updateDelaySeconds = updateDelaySeconds;
        settingExecutor();
    }

    private void settingExecutor() {
        executor.scheduleAtFixedRate(this::run, 0, updateDelaySeconds, TimeUnit.SECONDS);
    }

    private void run() {
        long tsFrom = lastSyncRepository.getSchedulerLastSync(schedulerType) ;
        long tsTo = Math.min(tsFrom + updateRangeMillis, currentTimeMillis());

        deltaUpdateService.updateDeltaEntity(tsFrom, tsTo);

        lastSyncRepository.setSchedulerLastSync(schedulerType, tsTo - rangeCrossingMillis);
    }
}
