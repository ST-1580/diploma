package com.st1580.diploma.updater.caller;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.external.alpha.data.AlphaEntityEvent;
import com.st1580.diploma.external.alpha.AlphaServiceApi;
import com.st1580.diploma.external.alpha.data.AlphaToBetaLinkEvent;
import org.springframework.stereotype.Service;

@Service
public class AlphaCaller {
    private final AlphaServiceApi alphaServiceApi;

    @Inject
    public AlphaCaller(AlphaServiceApi alphaServiceApi) {
        this.alphaServiceApi = alphaServiceApi;
    }

    public List<AlphaEntityEvent> getAlphaEntityEvents(long tsFrom, long tsTo) {
        return alphaServiceApi.getAlphaEntityEvents(tsFrom, tsTo);
    }

    public List<AlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(long tsFrom, long tsTo) {
        return alphaServiceApi.getAlphaToBetaLinkEvents(tsFrom, tsTo);
    }
}
