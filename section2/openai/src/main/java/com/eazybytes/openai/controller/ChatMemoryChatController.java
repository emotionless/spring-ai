package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class ChatMemoryChatController {

    private final ChatClient chatClient;

    public ChatMemoryChatController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> chat(@RequestHeader("username") String username,
                                       @RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient
                .prompt()
                .user(message)
                .advisors(
                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                )
                .call()
                .content());
    }
}
