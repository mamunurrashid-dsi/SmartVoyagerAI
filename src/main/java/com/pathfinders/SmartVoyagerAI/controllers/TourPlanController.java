package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class TourPlanController {

    @Autowired
    private SummaryAgent summaryAgent;

    @GetMapping("/")
    public String home() {
        return "form";
    }

    @GetMapping("/process")
    public Flux<String> process(@RequestParam String userInput) throws MessagingException {
        return summaryAgent.prepareAndSendSummary(userInput);

    }
}
