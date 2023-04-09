package com.st1580.diploma.repository;


import com.st1580.diploma.collector.repository.EntityCollectorRepository;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.repository.EntityEventRepository;

public interface AlphaRepository extends EntityCollectorRepository, EntityEventRepository<AlphaEvent> {

}
