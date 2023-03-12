package com.st1580.diploma.updater.caller;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.external.beta.BetaServiceApi;
import com.st1580.diploma.external.beta.data.ExternalBetaEntityEvent;
import org.springframework.stereotype.Service;

@Service
public class BetaCaller {
    private final BetaServiceApi betaServiceApi;

    @Inject
    public BetaCaller(BetaServiceApi betaServiceApi) {
        this.betaServiceApi = betaServiceApi;
    }

    public List<ExternalBetaEntityEvent> getBetaEntityEvents(long tsFrom, long tsTo) {
        return betaServiceApi.getBetaEntityEvents(tsFrom, tsTo);
    }
}
