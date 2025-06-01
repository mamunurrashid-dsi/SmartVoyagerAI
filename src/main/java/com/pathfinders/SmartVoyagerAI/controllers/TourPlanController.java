package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.EmailService;
import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
public class TourPlanController {

    @Autowired
    private SummaryAgent summaryAgent;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String home() {
        return "form";
    }

    @PostMapping("/process")
    public String process(@RequestParam String userInput) throws MessagingException {
        String summary = summaryAgent.prepareAndSendSummary(userInput);
        return summary;
    }

//    @PostMapping("/input")
//    public String extractInputData(@RequestParam String userInput) throws MessagingException {
//        String summary = summaryAgent.extractInformationFromUserInput(userInput);
//        return summary;
//    }

    @PostMapping("/sendEmail")
    public String sendEmail(
            @RequestParam String emailAddress,
            @RequestBody String content) {

        String subject = "SmartVoyagerAI - Your tour plan is ready";
        emailService.sendEmail(emailAddress, content, subject);
        return "Email sent to " + emailAddress;
    }
}
