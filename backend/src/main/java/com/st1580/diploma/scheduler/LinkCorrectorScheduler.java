package com.st1580.diploma.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.st1580.diploma.repository.LastSyncRepository;
import com.st1580.diploma.updater.service.LinkCorrectorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkCorrectorScheduler {
    private final static SchedulerType schedulerType = SchedulerType.LINK_CORRECTOR;
    private final ScheduledThreadPoolExecutor executor;
    private final LinkCorrectorService linkCorrectorService;
    private final LastSyncRepository lastSyncRepository;
    private final long updateDelaySeconds;

    @Inject
    public LinkCorrectorScheduler(LinkCorrectorService linkCorrectorService,
                                  LastSyncRepository lastSyncRepository,
                                  @Value("${scheduler.link.corrector.update.delay.seconds}") long updateDelaySeconds) {
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.linkCorrectorService = linkCorrectorService;
        this.lastSyncRepository = lastSyncRepository;
        this.updateDelaySeconds = updateDelaySeconds;
        settingExecutor();
    }

    private void settingExecutor() {
        executor.scheduleAtFixedRate(this::run, 0, updateDelaySeconds, TimeUnit.SECONDS);
    }

    private void run() {
        long tsFrom = lastSyncRepository.getCorrectorLastSync();
        long tsTo = lastSyncRepository.getServicesUpdateTs();

        linkCorrectorService.addLinkEventsByEntityUpdate(tsFrom, tsTo);
        linkCorrectorService.correctLinks(tsFrom, tsTo);

        lastSyncRepository.setSchedulerLastSync(schedulerType, tsTo);
    }
}
