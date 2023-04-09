package com.st1580.diploma.repository;

import com.st1580.diploma.collector.repository.EntityCollectorRepository;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.repository.EntityEventRepository;

public interface DeltaRepository extends EntityCollectorRepository, EntityEventRepository<DeltaEvent> {

}
