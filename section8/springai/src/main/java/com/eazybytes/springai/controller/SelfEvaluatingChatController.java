package com.eazybytes.springai.controller;


import com.eazybytes.springai.exception.InvalidException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SelfEvaluatingChatController {

    private final ChatClient chatClient;
    private RelevancyEvaluator relevancyEvaluator;

    @Value("classpath:/promptTemplates/hrPolicy.st")
    Resource hrPolicyTemplate;

    public SelfEvaluatingChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @GetMapping("/self-evaluation/chat")
    public String chat(@RequestParam("message") String message) {
        String response = chatClient.prompt().user(message)
                .call().content();
        validateAnswer(message, response);
        return response;
    }

    @GetMapping("/self-evaluation/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        String response = chatClient
                .prompt().system(hrPolicyTemplate)
                .user(message)
                .call().content();
        validateAnswer(message, response);
        return response;
    }

    private void validateAnswer(String message, String answer) {
        EvaluationRequest evaluationRequest = new EvaluationRequest(message, answer);
        EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);

        if (!response.isPass()) {
            throw new InvalidException(message, answer);
        }
    }
}