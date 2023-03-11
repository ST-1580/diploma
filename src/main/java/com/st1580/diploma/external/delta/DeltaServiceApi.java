package com.st1580.diploma.external.delta;

import java.util.List;
import java.util.Map;

import com.st1580.diploma.external.beta.data.BetaEntityEvent;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import com.st1580.diploma.external.delta.data.DeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/v1/delta")
public interface DeltaServiceApi {
    @PostMapping("/create/entity")
    String createEntity(@RequestBody ExternalDeltaEntity newEntity);

    @PatchMapping("/patch/entity")
    String patchEntity(@RequestBody ExternalDeltaEntity deltaEntity);

    @DeleteMapping("/delete/entity")
    String deleteEntity(@RequestParam("id") long entityId);

    @GetMapping("/events/entity")
    Map<DeltaEventType, List<DeltaEntityEvent>> getDeltaEntityEvents(@RequestParam("from") long tsFrom,
                                                                     @RequestParam("to") long tsTo);
}
