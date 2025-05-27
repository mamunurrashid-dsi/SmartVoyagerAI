package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import com.pathfinders.SmartVoyagerAI.services.UserInputAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flux")
public class TourPlanController {

    private final SummaryAgent summaryAgent;
    private final UserInputAgent userInputAgent;

    public TourPlanController(SummaryAgent summaryAgent, UserInputAgent userInputAgent) {
        this.summaryAgent = summaryAgent;
        this.userInputAgent = userInputAgent;
    }

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("form");
    }

    @PostMapping(value = "/process", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> process(@RequestParam String userInput) {

        return summaryAgent.prepareAndSendSummaryOnFlux(userInput);
    }

    @PostMapping("/extract")
    public Mono<String> extract(@RequestParam String userInput) {
//        System.out.println(userInputAgent.extractInformationFromUserInput(userInput));
        return userInputAgent.extractInformationFromUserInput(userInput);
    }
}
