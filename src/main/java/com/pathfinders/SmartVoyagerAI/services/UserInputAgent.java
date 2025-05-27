package com.pathfinders.SmartVoyagerAI.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserInputAgent {
    private final ChatClient chatClient;

    public UserInputAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public Mono<String> extractInformationFromUserInput(String userInput) {
        String systemPrompt = """
            You are a travel assistant. Your job is to extract the following fields from the user's input:
            - name
            - destination
            - startDate (Full date e.g 2025-05-27)
            - endDate (Full date e.g 2025-06-30)
            - budget (e.g., low, mid-range, luxury)
            - interests (comma-separated values like history, adventure, food, etc.)
            - pace (relaxed, packed, or moderate)
            
            Return the result as a JSON object. If any field is missing or unclear, leave it as null.
            If only month is provided in startDate and endDate append current year with that month (e.g July-2025).
            """;

        Prompt prompt = new Prompt(
                new SystemMessage(systemPrompt),
                new UserMessage(userInput)
        );

        return chatClient.prompt(prompt)
                .stream() // non-blocking
                .content() // Flux<String>
                .collectList() // gather into a List<String>
                .map(list -> {
                    String response = String.join("", list);
                    System.out.println("extracted json value \n" + response);

                    // extract the ```json ... ``` block
                    int start = response.indexOf("```json");
                    int end = response.lastIndexOf("```");

                    if (start != -1 && end != -1 && end > start) {
                        return response.substring(start + 7, end).trim(); // exclude ```json
                    } else {
                        return "{}"; // fallback
                    }
                });
    }


}
