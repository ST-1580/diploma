package com.st1580.diploma.external.gamma;

import java.util.List;

import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityDto;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkDto;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkEvent;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkDto;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/v1/gamma")
@CrossOrigin(origins = "http://localhost:3000")
public interface GammaServiceApi {
    // ------------- gamma entity

    @GetMapping("/entities/active")
    List<ExternalGammaEntityDto> getAllActiveGammaEntities();

    @GetMapping("/entities/disable")
    List<ExternalGammaEntityDto> getAllDisableGammaEntities();

    @PostMapping("/create/entity")
    String createEntity(@RequestBody ExternalGammaEntityDto newEntity);

    @PatchMapping("/patch/entity")
    String patchEntity(@RequestBody ExternalGammaEntityDto gammaEntity);

    @PostMapping("/switch/entity")
    String switchEntity(@RequestParam("id") long entityId);

    // ------------- gamma -> alpha link

    @GetMapping("/link/alpha/active")
    List<ExternalGammaToAlphaLinkDto> getAllActiveGammaToAlphaLinks();

    @GetMapping("/link/alpha/disable")
    List<ExternalGammaToAlphaLinkDto> getAllDisableGammaToAlphaLinks();

    @PostMapping("/create/link/alpha")
    String createGammaToAlphaLink(@RequestBody ExternalGammaToAlphaLinkDto newLink);

    @PatchMapping("/patch/link/alpha")
    String patchGammaToAlphaLink(@RequestBody ExternalGammaToAlphaLinkDto gammaToAlphaLink);

    @PostMapping("/switch/link/alpha")
    String switchGammaToAlphaLink(@RequestParam("gammaId") long gammaId, @RequestParam("alphaId") long alphaId);

    // ------------- gamma -> delta link

    @GetMapping("/link/delta/active")
    List<ExternalGammaToDeltaLinkDto> getAllActiveGammaToDeltaLinks();

    @GetMapping("/link/delta/disable")
    List<ExternalGammaToDeltaLinkDto> getAllDisableGammaToDeltaLinks();

    @PostMapping("/create/link/delta")
    String createGammaToDeltaLink(@RequestBody ExternalGammaToDeltaLinkDto newLink);

    @PatchMapping("/patch/link/delta")
    String patchGammaToDeltaLink(@RequestBody ExternalGammaToDeltaLinkDto gammaToDeltaLink);

    @PostMapping("/switch/link/delta")
    String switchGammaToDeltaLink(@RequestParam("gammaId") long gammaId, @RequestParam("deltaId") long deltaId);

    // ------------- space update

    @GetMapping("/events/entity")
    List<ExternalGammaEntityEvent> getGammaEntityEvents(@RequestParam("from") long tsFrom,
                                                        @RequestParam("to") long tsTo);

    @GetMapping("/events/link/alpha")
    List<ExternalGammaToAlphaLinkEvent> getGammaToAlphaLinkEvents(@RequestParam("from") long tsFrom,
                                                                  @RequestParam("to") long tsTo);

    @GetMapping("/events/link/delta")
    List<ExternalGammaToDeltaLinkEvent> getGammaToDeltaLinkEvents(@RequestParam("from") long tsFrom,
                                                                  @RequestParam("to") long tsTo);
}
