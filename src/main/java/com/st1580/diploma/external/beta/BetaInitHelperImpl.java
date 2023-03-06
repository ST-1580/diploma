package com.st1580.diploma.external.beta;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.BetaRepository;
import org.springframework.stereotype.Service;

@Service
public class BetaInitHelperImpl implements BetaInitHelper {
    @Inject
    private BetaRepository betaRepository;
    @Override
    public List<Long> getAllEntityIds() {
        return betaRepository.getAllIds();
    }
}
