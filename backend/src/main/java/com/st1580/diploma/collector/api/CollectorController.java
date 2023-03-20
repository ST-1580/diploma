package com.st1580.diploma.collector.api;

import java.util.List;

import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface CollectorController {
    @GetMapping("/all/{id}")
    GraphDto collectGraph(@PathVariable("id") long entityId,
                          @RequestParam(value = "policy", defaultValue = "start", required = false) PolicyType policyType,
                          @RequestParam(value = "ll", defaultValue = "true", required = false) boolean isLinksLight,
                          @RequestParam(value = "le", defaultValue = "true", required = false) boolean isEntitiesLight);

    @GetMapping("/neighbors/{id}")
    List<GraphLinkDto> collectEntityNeighbors(@PathVariable("id") long entityId,
                                              @RequestParam(value = "ll", defaultValue = "false", required = false) boolean isLinksLight);
}
