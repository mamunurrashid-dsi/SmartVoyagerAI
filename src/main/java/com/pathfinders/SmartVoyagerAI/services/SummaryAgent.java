package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SummaryAgent {
//    @Autowired
//    private JavaMailSender mailSender;

//    @Autowired
//    private ToolCallingChatMo toolCallingChatModel;

    private final ChatClient chatClient;

    @Autowired
    @Lazy
    private WeatherAgent weatherAgent;

    @Autowired
    @Lazy
    private FlightAgent flightAgent;

    public SummaryAgent(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }


    public String prepareAndSendSummary(String destination, String date, String email) throws MessagingException {

//        List<ToolSpecification> toolSpecs = ToolSpecifications.fromBeans(weatherAgent, flightAgent);

        Prompt prompt = new Prompt(
                new SystemMessage("You are a helpful travel agent."),
                new UserMessage("Get the weather and cheapest flight for %s on %s.".formatted(destination, date))
        );

        // Auto-detects @Tool-annotated methods

//        sendEmail(email, summary);
        return chatClient.prompt(prompt)
                .tools(weatherAgent,flightAgent) // Auto-detects @Tool-annotated methods
                .call()
                .content();
    }

    public Mono<String> prepareAndSendSummaryOnFlux(String destination, String date, String email) throws MessagingException {

        return Mono.fromCallable(() -> {
            Prompt prompt = new Prompt(
                    new SystemMessage("You are a helpful travel agent."),
                    new UserMessage("Get the weather and cheapest flight for %s on %s.".formatted(destination, date))
            );

            // Blocking call to the chat client
            String summary = chatClient.prompt(prompt)
                    .tools(weatherAgent, flightAgent)
                    .call()
                    .content();

//            sendEmail(email, summary);
            return summary;
        }).subscribeOn(Schedulers.boundedElastic()); // Runs blocking work on a background thread
    }

//    public void sendEmail(String to, String body) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject("Your Trip Summary");
//        helper.setText(body);
//        mailSender.send(message);
//    }
}
