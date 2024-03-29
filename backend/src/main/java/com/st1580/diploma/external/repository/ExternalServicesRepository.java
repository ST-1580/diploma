package com.st1580.diploma.external.repository;

import java.util.List;

import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntity;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLink;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLink;

public interface ExternalServicesRepository {
    List<ExternalAlphaEntity> getAllAlphaEntities();

    List<ExternalAlphaToBetaLink> getAllAlphaToBetaLinks();

    List<ExternalBetaEntity> getAllBetaEntities();

    List<ExternalGammaEntity> getAllGammaEntities();

    List<ExternalGammaToAlphaLink> getAllGammaToAlphaLinks();

    List<ExternalGammaToDeltaLink> getAllGammaToDeltaLinks();

    List<ExternalDeltaEntity> getAllDeltaEntities();
}
