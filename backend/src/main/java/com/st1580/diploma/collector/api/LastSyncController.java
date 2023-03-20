package com.st1580.diploma.collector.api;

import com.st1580.diploma.scheduler.SchedulerType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/lastsync")
public interface LastSyncController {

    @GetMapping("")
    long getServiceLastSync(@RequestParam("type") SchedulerType type);
}
