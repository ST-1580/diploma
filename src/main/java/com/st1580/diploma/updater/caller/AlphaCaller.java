package com.st1580.diploma.updater.caller;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.alpha.AlphaServiceApi;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;
import org.springframework.stereotype.Service;

@Service
public class AlphaCaller {
    private final AlphaServiceApi alphaServiceApi;

    @Inject
    public AlphaCaller(AlphaServiceApi alphaServiceApi) {
        this.alphaServiceApi = alphaServiceApi;
    }

    public List<ExternalAlphaEntityEvent> getAlphaEntityEvents(long tsFrom, long tsTo) {
        return alphaServiceApi.getAlphaEntityEvents(tsFrom, tsTo);
    }

    public List<ExternalAlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(long tsFrom, long tsTo) {
        return alphaServiceApi.getAlphaToBetaLinkEvents(tsFrom, tsTo);
    }
}
