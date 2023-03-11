package com.st1580.diploma.external.delta;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.DeltaRepository;
import org.springframework.stereotype.Service;

@Service
public class DeltaInitHelperImpl implements DeltaInitHelper {
    @Inject
    private DeltaRepository deltaRepository;
    @Override
    public List<Long> getAllDeltaEntities() {
        return new ArrayList<>();
//        return deltaRepository.getAllIds();
    }
}
