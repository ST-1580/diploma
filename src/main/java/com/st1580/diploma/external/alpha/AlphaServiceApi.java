package com.st1580.diploma.external.alpha;

import java.util.List;

import com.st1580.diploma.external.alpha.data.AlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.AlphaToBetaLinkEvent;
import com.st1580.diploma.external.alpha.data.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.ExternalAlphaToBetaLink;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/v1/alpha")
public interface AlphaServiceApi {
    @PostMapping("/create/entity")
    String createEntity(@RequestBody ExternalAlphaEntity alphaEntity);

    @PatchMapping("/patch/entity")
    String patchEntity(@RequestBody ExternalAlphaEntity newEntity);

    @DeleteMapping("/delete/entity")
    String deleteEntity(@RequestParam("id") long entityId);

    @PostMapping("/create/link/beta")
    String createAlphaToBetaLink(@RequestBody ExternalAlphaToBetaLink alphaToBetaLink);

    @PatchMapping("/patch/link/beta")
    String patchAlphaToBetaLink(@RequestBody ExternalAlphaToBetaLink newLink);

    @DeleteMapping("/delete/link/beta")
    String deleteAlphaToBetaLink(@RequestParam("alphaId") long alphaId, @RequestParam("betaId") long betaId);

    @GetMapping("/events/entity")
    List<AlphaEntityEvent> getAlphaEntityEvents(@RequestParam("from") long tsFrom,
                                                @RequestParam("to") long tsTo);

    @GetMapping("/events/link/beta")
    List<AlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(@RequestParam("from") long tsFrom,
                                                        @RequestParam("to") long tsTo);
}
