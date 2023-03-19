package com.st1580.diploma.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.st1580.diploma.repository.LastSyncRepository;
import com.st1580.diploma.updater.service.GammaUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class GammaScheduler {
    private final static SchedulerType schedulerType = SchedulerType.GAMMA;
    private final ScheduledThreadPoolExecutor executor;
    private final GammaUpdateService gammaUpdateService;
    private final LastSyncRepository lastSyncRepository;
    private final long rangeCrossingMillis;
    private final long updateRangeMillis;
    private final long updateDelaySeconds;

    @Inject
    public GammaScheduler(GammaUpdateService gammaUpdateService,
                          LastSyncRepository lastSyncRepository,
                          @Value("${scheduler.gamma.range.crossing.seconds}") long rangeCrossingSeconds,
                          @Value("${scheduler.gamma.update.range.minutes}") long updateRangeMinutes,
                          @Value("${scheduler.gamma.update.delay.seconds}") long updateDelaySeconds) {
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.gammaUpdateService = gammaUpdateService;
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
        long tsFrom = lastSyncRepository.getSchedulerLastSync(schedulerType);
        long tsTo = Math.min(tsFrom + updateRangeMillis, currentTimeMillis());

        gammaUpdateService.updateGammaEntity(tsFrom, tsTo);
        gammaUpdateService.updateGammaToAlphaLink(tsFrom, tsTo);
        gammaUpdateService.updateGammaToDeltaLink(tsFrom, tsTo);

        lastSyncRepository.setSchedulerLastSync(schedulerType, tsTo - rangeCrossingMillis);
    }
}
