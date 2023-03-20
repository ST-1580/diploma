package com.st1580.diploma.updater.caller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.st1580.diploma.external.delta.DeltaServiceApi;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import org.springframework.stereotype.Service;

@Service
public class DeltaCaller {
    @Inject
    private DeltaServiceApi deltaServiceApi;

    public List<ExternalDeltaEntityEvent> getAllDeltaEvents(long tsFrom, long tsTo) {
        return deltaServiceApi.getDeltaEntityEvents(tsFrom, tsTo);
    }
}
