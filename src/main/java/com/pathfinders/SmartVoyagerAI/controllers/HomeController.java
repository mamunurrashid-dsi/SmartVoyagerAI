package com.pathfinders.SmartVoyagerAI.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    @GetMapping("/plan")
    public String getChat() {
        return "chat";
    }
}
