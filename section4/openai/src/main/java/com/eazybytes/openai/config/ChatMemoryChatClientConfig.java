package com.eazybytes.openai.config;

import com.eazybytes.openai.advisors.TokenUsageAuditAdvisor;
import com.eazybytes.openai.rag.PIIMaskingDocumentPostProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.DocumentWriter;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryChatClientConfig {

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    @Bean("chatMemoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor, DocumentWriter documentWriter) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor, tokenUsageAdvisor, retrievalAugmentationAdvisor))
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3).similarityThreshold(0.5).build();
        return RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english").build())
                .documentRetriever(vectorStoreDocumentRetriever)
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
                .build();
    }
}
