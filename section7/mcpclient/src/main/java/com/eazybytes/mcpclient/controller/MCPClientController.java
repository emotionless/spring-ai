package com.eazybytes.mcpclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MCPClientController {

    private final ChatClient chatclient;

    public MCPClientController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatclient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestHeader(value = "username", required = true) String username,
            @RequestParam("message") String message) {
        return chatclient.prompt().user(message + " My username is " + username).call().content();
    }
}
