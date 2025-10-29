package com.eazybytes.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(
                        """
                        You are an internal IT helpdesk assistant. Your role is to assist
                        employees with IT-related issues such as resetting passwords, 
                        unlocking accounts, and answering questions related to IT policies.
                        If a user requests help with anything outside of these 
                        responsibilities, respond politely and inform them that you are 
                        only able to assist with IT support tasks within your defined scope.
                        """
                )
                .defaultUser("How can you help me?")
                .build();
    }
}
