package com.st1580.diploma.external.alpha;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.external.alpha.data.AlphaBetaId;
import org.springframework.stereotype.Service;

@Service
public class InitHelperImpl implements InitHelper {
    @Inject
    private AlphaRepository alphaRepository;
    @Inject
    private AlphaToBetaRepository alphaToBetaRepository;

    public List<Long> getAllEntityIds() {
        return alphaRepository.getAllIds();
    }

    public List<AlphaBetaId> getAllLinksIds() {
        return alphaToBetaRepository.getAllLinks()
                .stream()
                .map(link -> new AlphaBetaId(link.getAlphaId(), link.getBetaId()))
                .collect(Collectors.toList());
    }
}
