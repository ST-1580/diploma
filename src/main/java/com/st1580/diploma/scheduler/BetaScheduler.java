package com.st1580.diploma.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.st1580.diploma.repository.LastSyncRepository;
import com.st1580.diploma.updater.service.BetaUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class BetaScheduler {
    private final static SchedulerType schedulerType = SchedulerType.BETA;
    private final ScheduledThreadPoolExecutor executor;
    private final BetaUpdateService betaUpdateService;
    private final LastSyncRepository lastSyncRepository;
    private final long rangeCrossingMillis;
    private final long updateRangeMillis;
    private final long updateDelaySeconds;

    @Inject
    public BetaScheduler(BetaUpdateService betaUpdateService,
                          LastSyncRepository lastSyncRepository,
                          @Value("${scheduler.beta.range.crossing.seconds}") long rangeCrossingSeconds,
                          @Value("${scheduler.beta.update.range.minutes}") long updateRangeMinutes,
                          @Value("${scheduler.beta.update.delay.seconds}") long updateDelaySeconds) {
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.betaUpdateService = betaUpdateService;
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

        betaUpdateService.updateBetaEntity(tsFrom, tsTo);

        lastSyncRepository.setSchedulerLastSync(schedulerType, tsTo - rangeCrossingMillis);
    }
}
