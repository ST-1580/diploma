package com.st1580.diploma.repository;

import com.st1580.diploma.collector.repository.LinkCollectorRepository;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.repository.LinkEventRepository;

public interface AlphaToBetaRepository extends LinkCollectorRepository,
        LinkEventRepository<AlphaToBetaEvent, AlphaEvent, BetaEvent> {

}
