package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flux")
public class TourPlanController {

    private final SummaryAgent summaryAgent;

    public TourPlanController(SummaryAgent summaryAgent) {
        this.summaryAgent = summaryAgent;
    }

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("form");
    }

    @PostMapping(value = "/process", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> process(@RequestParam String destination,
                                @RequestParam String date,
                                @RequestParam String email) {

        return summaryAgent.prepareAndSendSummaryOnFlux(destination, date, email);
    }
}
