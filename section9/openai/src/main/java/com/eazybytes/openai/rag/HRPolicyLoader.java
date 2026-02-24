package com.eazybytes.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.eazybytes.openai.rag.BasicCodeChunker.chunkSource;

@Component
public class HRPolicyLoader {

    private final VectorStore vectorStore;

    @Value("classpath:Eazybytes_HR_Policies.pdf")
    Resource policyDocument;

    public HRPolicyLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadPDF() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyDocument);
        List<Document> documents = tikaDocumentReader.get();
        TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(200).build();
        vectorStore.add(textSplitter.split(documents));
    }
}
