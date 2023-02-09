package com.st1580.diploma.collector.api;

import java.util.List;

import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CollectorController {
    @GetMapping("/all/{id}")
    GraphDto collectAll(@PathVariable("id") long entityId);

    @GetMapping("/neighbors/{id}")
    List<GraphLinkDto> collectEntityNeighbors(@PathVariable("id") long entityId);
}
