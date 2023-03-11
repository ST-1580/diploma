package com.st1580.diploma.updater.caller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.st1580.diploma.external.gamma.GammaServiceApi;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;
import com.st1580.diploma.external.gamma.data.GammaEventType;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkEvent;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkEvent;
import org.springframework.stereotype.Service;

@Service
public class GammaCaller {
    private final GammaServiceApi gammaServiceApi;

    @Inject
    public GammaCaller(GammaServiceApi gammaServiceApi) {
        this.gammaServiceApi = gammaServiceApi;
    }

    public List<ExternalGammaEntityEvent> getGammaEntityEvents(long tsFrom, long tsTo) {
        return gammaServiceApi.getGammaEntityEvents(tsFrom, tsTo);
    }

    public List<ExternalGammaToAlphaLinkEvent> getAllGammaToAlphaLinkEvents(long tsFrom, long tsTo) {
        return gammaServiceApi.getGammaToAlphaLinkEvents(tsFrom, tsTo);
    }

    public List<ExternalGammaToDeltaLinkEvent> getAllGammaToDeltaLinkEvents(long tsFrom, long tsTo) {
        return gammaServiceApi.getGammaToDeltaLinkEvents(tsFrom, tsTo);
    }
}
