package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserInputAgent {
    private final ChatClient chatClient;

    public UserInputAgent(ChatModel chatModel, ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
//    @Tool(name = "get-user-information", description = "Extract user information from userInput")
    public String extractInformationFromUserInput(String userInput) {
        String systemPrompt = """
                You are a travel assistant. Your job is to extract the following fields from the user's input:
                - name
                - currentLocation
                - destination
                - startDate (Full date e.g 2025-05-27)
                - endDate (Full date e.g 2025-06-30)
                - budget (e.g., low, mid-range, luxury)
                - interests (comma-separated values like history, adventure, food, etc.)
                - pace (relaxed, packed, or moderate)
                
                Current date is %s. So if user doesn't input exact date then use current date to figure out their
                start data and end date of the tour.
                If only month is provided in startDate and endDate append current year with that month (e.g July-2025).
                
                Respond ONLY in this JSON format without explanation. If data is missing for any field keep it null.
                Expected JSON response format:
                  {
                    "name": "John Doe",
                    "currentLocation": "New York",
                    "destination": "Paris",
                    "startDate": "2025-05-27",
                    "endDate": "2025-06-30",
                    "budget": "mid-range",
                    "interests": "art, food, sightseeing",
                    "pace": "moderate"
                  }
                """.formatted(LocalDate.now());
        Prompt prompt = new Prompt(
                new SystemMessage(systemPrompt),
                new UserMessage(userInput)
        );
        String response = chatClient.prompt(prompt)
                .call()
                .content();
        assert response != null;
        System.out.println("user input extracted === "+response);
        return response;
//        System.out.println("extracted json value \n"+response.substring(response.indexOf("```json"), response.lastIndexOf("```")));
//        return response.substring(response.indexOf("```json"), response.lastIndexOf("```"));
    }
}
