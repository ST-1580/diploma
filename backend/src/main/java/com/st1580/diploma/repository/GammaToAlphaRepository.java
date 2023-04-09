package com.st1580.diploma.repository;

import com.st1580.diploma.collector.repository.LinkCollectorRepository;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;
import com.st1580.diploma.updater.repository.LinkEventRepository;

public interface GammaToAlphaRepository extends LinkCollectorRepository,
        LinkEventRepository<GammaToAlphaEvent, GammaEvent, AlphaEvent> {

}
