package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TourPlanController {

    @Autowired
    private SummaryAgent summaryAgent;

    @GetMapping("/")
    public String home() {
        return "form";
    }

    @PostMapping("/process")
    public String process(@RequestParam String userInput) throws MessagingException {
        String summary = summaryAgent.prepareAndSendSummary(userInput);
        return summary;
    }
}
