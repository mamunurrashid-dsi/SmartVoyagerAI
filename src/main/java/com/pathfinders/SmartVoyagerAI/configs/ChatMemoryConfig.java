package com.pathfinders.SmartVoyagerAI.configs;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {
//    private final ChatMemoryRepository chatMemoryRepository;
//
//    public ChatMemoryConfig(ChatMemoryRepository chatMemoryRepository) {
//        this.chatMemoryRepository = chatMemoryRepository;
//    }

    @Bean
    public ChatMemory chatMemory() {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
        return chatMemory;
    }
}
