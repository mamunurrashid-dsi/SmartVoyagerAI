package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SummaryAgent {
//    @Autowired
//    private JavaMailSender mailSender;

    private final ChatClient chatClient;

    @Autowired
    @Lazy
    private WeatherAgent weatherAgent;

    @Autowired
    @Lazy
    private FlightAgent flightAgent;

    @Autowired
    @Lazy
    private UserInputAgent userInputAgent;

    public SummaryAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public Flux<String> prepareAndSendSummary(String userInput) {
        String systemMessage = """
            You are a professional travel planner. Based on the user preferences in JSON format, generate a detailed, engaging travel itinerary.
            
            Instructions:
            - Show weather info and flight info. Use tools for these information.
            - Create a personalized daily itinerary for the trip.
            - Include recommendations for places to visit, things to do, and local food to try.
            - Consider the user's interests and budget.
            - Include travel tips where appropriate.
            - Format the output clearly, by day (e.g., Day 1, Day 2...).
            """;

        // Assuming this returns Mono<String>
        return userInputAgent.extractInformationFromUserInput(userInput)
                .flatMapMany(userInputMessage -> {
                    Prompt prompt = new Prompt(
                            new SystemMessage(systemMessage),
                            new UserMessage(userInputMessage)
                    );

                    return chatClient.prompt(prompt)
                            .tools(weatherAgent, flightAgent)
                            .stream()
                            .content(); // returns Flux<String>
                });
    }

//            sendEmail(email, summary);



//    public String prepareAndSendSummary(String destination, String date, String email) throws MessagingException {
//
////        List<ToolSpecification> toolSpecs = ToolSpecifications.fromBeans(weatherAgent, flightAgent);
//
//        Prompt prompt = new Prompt(
//                new SystemMessage("You are a helpful travel agent."),
//                new UserMessage("Get the weather and cheapest flight for %s on %s.".formatted(destination, date))
//        );
//
////        sendEmail(email, summary);
//        return chatClient.prompt(prompt)
//                .tools(weatherAgent,flightAgent) // Auto-detects @Tool-annotated methods
//                .call()
//                .content();
//    }
//
//    }


//    public void sendEmail(String to, String body) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject("Your Trip Summary");
//        helper.setText(body);
//        mailSender.send(message);
//    }
}
