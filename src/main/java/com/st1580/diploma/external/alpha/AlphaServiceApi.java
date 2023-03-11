package com.st1580.diploma.external.alpha;

import java.util.List;

import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityDto;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkDto;
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
    String createEntity(@RequestBody ExternalAlphaEntityDto newEntity);

    @PatchMapping("/patch/entity")
    String patchEntity(@RequestBody ExternalAlphaEntityDto alphaEntity);

    @PostMapping("/switch/entity")
    String switchEntity(@RequestParam("id") long entityId);

    @PostMapping("/create/link/beta")
    String createAlphaToBetaLink(@RequestBody ExternalAlphaToBetaLinkDto newLink);

    @PatchMapping("/patch/link/beta")
    String patchAlphaToBetaLink(@RequestBody ExternalAlphaToBetaLinkDto alphaToBetaLink);

    @PostMapping("/switch/link/beta")
    String switchAlphaToBetaLink(@RequestParam("alphaId") long alphaId, @RequestParam("betaId") long betaId);

    @GetMapping("/events/entity")
    List<ExternalAlphaEntityEvent> getAlphaEntityEvents(@RequestParam("from") long tsFrom,
                                                        @RequestParam("to") long tsTo);

    @GetMapping("/events/link/beta")
    List<ExternalAlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(@RequestParam("from") long tsFrom,
                                                                @RequestParam("to") long tsTo);
}
