package com.st1580.diploma.updater.caller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.st1580.diploma.external.delta.DeltaServiceApi;
import com.st1580.diploma.external.delta.data.DeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import org.springframework.stereotype.Service;

@Service
public class DeltaCaller {
    @Inject
    private DeltaServiceApi deltaServiceApi;

    public Map<DeltaEventType, List<DeltaEntityEvent>> getAllDeltaEvents(long fromTs, long toTs) {
        return deltaServiceApi.getDeltaEntityEvents(fromTs, toTs);
    }
}
