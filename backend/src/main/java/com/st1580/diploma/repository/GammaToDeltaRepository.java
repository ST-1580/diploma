package com.st1580.diploma.repository;

import com.st1580.diploma.collector.repository.LinkCollectorRepository;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;
import com.st1580.diploma.updater.repository.LinkEventRepository;

public interface GammaToDeltaRepository extends LinkCollectorRepository,
        LinkEventRepository<GammaToDeltaEvent, GammaEvent, DeltaEvent> {

}
