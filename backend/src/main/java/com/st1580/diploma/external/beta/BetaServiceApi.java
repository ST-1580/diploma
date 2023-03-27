package com.st1580.diploma.external.beta;

import java.util.List;

import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import com.st1580.diploma.external.beta.data.ExternalBetaEntityEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/v1/beta")
@CrossOrigin(origins = "http://localhost:3000")
public interface BetaServiceApi {
    @GetMapping("/entities/active")
    List<ExternalBetaEntity> getAllActiveBetaEntities();

    @PostMapping("/create/entity")
    String createEntity(@RequestBody ExternalBetaEntity newEntity);

    @PatchMapping("/patch/entity")
    String patchEntity(@RequestBody ExternalBetaEntity betaEntity);

    @DeleteMapping("/delete/entity")
    String deleteEntity(@RequestParam("id") long entityId);

    @GetMapping("/events/entity")
    List<ExternalBetaEntityEvent> getBetaEntityEvents(@RequestParam("from") long tsFrom,
                                                      @RequestParam("to") long tsTo);
}
