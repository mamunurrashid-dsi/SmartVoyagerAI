package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/flux")
public class TourPlanFluxController {

    private final SummaryAgent summaryAgent;

    public TourPlanFluxController(SummaryAgent summaryAgent) {
        this.summaryAgent = summaryAgent;
    }

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("form");
    }

    @PostMapping(value = "/process")
    public Mono<String> process(@RequestParam String destination,
                                @RequestParam String date,
                                @RequestParam String email,
                                Model model) {

        return summaryAgent.prepareAndSendSummaryOnFlux(destination, date, email)
                .doOnNext(summary -> model.addAttribute("summary", summary))
                .thenReturn("summary");
    }
}
