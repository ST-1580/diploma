package com.st1580.diploma.updater.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corrector/")
public interface LinkCorrectorService {
    @GetMapping("/entity")
    void addLinkEventsByEntityUpdate(@RequestParam("from") long tsFrom, @RequestParam("to") long tsTo);

    @GetMapping("/link")
    void correctLinks(@RequestParam("from") long tsFrom, @RequestParam("to") long tsTo);
}
