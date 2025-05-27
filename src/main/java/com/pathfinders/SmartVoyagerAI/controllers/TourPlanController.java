package com.pathfinders.SmartVoyagerAI.controllers;

import com.pathfinders.SmartVoyagerAI.services.SummaryAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TourPlanController {

    @Autowired
    private SummaryAgent summaryAgent;

    @GetMapping("/")
    public String home() {
        return "form";
    }

    @PostMapping("/process")
    public String process(@RequestParam String destination,
                          @RequestParam String date,
                          @RequestParam String email,
                          Model model) throws MessagingException {
        String summary = summaryAgent.prepareAndSendSummary(destination, date, email);
        model.addAttribute("summary", summary);
        return "summary";
    }
}
