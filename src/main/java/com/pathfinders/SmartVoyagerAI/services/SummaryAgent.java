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

    @Autowired
    @Lazy
    private HotelAgent hotelAgent;

    public SummaryAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String prepareAndSendSummary(String userInput) throws MessagingException {
        String systemMessage = """
                You are a professional travel planner. Based on the user preferences in JSON format, generate a detailed, engaging travel itinerary.
                
                Instructions:
                - Show weather info, flight info and hotel info. Use tools for these information.
                    - While using tools extract associated parameters for tool calling from userInput.
                    - While getting hotels, you need to get the cityCode from the destination from userInput and then call the tool.
                    Include approximate costing on each suggested hotel and recommend which one is suitable for the budget in userInput.
                    - While getting flights, you need to get the originCityCode/IATA code from currentLocation and destinationCityCode/IATA code from destination
                    as well as startDate from userInput.
                - All types of currency values should be converted according to currentLocation from userInput.
                - Get an appropriate airport for the currentLocation on userInput.
                - Create a personalized daily itinerary for the trip.
                - Include recommendations for places to visit, things to do, and local food to try.
                - Consider the user's interests and budget.
                - Include travel tips where appropriate.
                - Format the output clearly, by day (e.g., Day 1, Day 2...).
                """;


            Prompt prompt = new Prompt(
                    new SystemMessage(systemMessage),
                    new UserMessage(userInputAgent.extractInformationFromUserInput(userInput))
            );
            String response = chatClient.prompt(prompt)
                    .tools(weatherAgent, flightAgent, hotelAgent)
                    .call()
                    .content();
        System.out.println("summary response: \n"+response);
        return response;
//            sendEmail(email, summary);

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
