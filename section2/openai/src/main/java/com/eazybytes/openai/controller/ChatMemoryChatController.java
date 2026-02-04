package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatMemoryChatController {

    private final ChatClient chatClient;

    public ChatMemoryChatController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> chat(@RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient
                .prompt()
                .user(message)
                .call()
                .content());
    }
}
