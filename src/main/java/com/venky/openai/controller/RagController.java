package com.venky.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {
  private final ChatClient chatClient;
  private final VectorStore vectorStore;
  private final ChatClient webSearchChatClient;

  @Value("classpath:/promptTemplate/systemPromptRandomDataTemplate.st")
  private Resource promptTemplate;

  @Value("classpath:/promptTemplate/systemPromptHrTemplate.st")
  private Resource hrPromptTemplateDocument;

  public RagController(
      @Qualifier("chatClientWithMemory") ChatClient chatClient,
      VectorStore vectorStore,
      @Qualifier("webSearchRAGChatClient") ChatClient webSearchChatClient) {
    this.chatClient = chatClient;
    this.vectorStore = vectorStore;
    this.webSearchChatClient = webSearchChatClient;
  }

  @GetMapping("/random/chat")
  public ResponseEntity<String> chatWithRag(
      @RequestParam("message") String message, @RequestHeader("username") String username) {
    //    SearchRequest searchRequest =
    //        SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
    //    List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);
    //    String similarContext =
    //        similarDocuments.stream()
    //            .map(Document::getText)
    //            .collect(Collectors.joining(System.lineSeparator()));
    String llmResponse =
        chatClient
            .prompt()
            //            .system(
            //                promptSystemSpec ->
            //                    promptSystemSpec.text(promptTemplate).param("documents",
            // similarContext))
            .advisors(
                advisorSpec ->
                    advisorSpec.param(
                        ChatMemory.CONVERSATION_ID,
                        username != null && !username.isEmpty() ? username : "default"))
            .user(message)
            .call()
            .content();

    return ResponseEntity.ok(llmResponse);
  }

  @GetMapping("/document/chat")
  public ResponseEntity<String> documentChatWithRag(
      @RequestParam("message") String message, @RequestHeader("username") String username) {
    //    SearchRequest searchRequest =
    //            SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
    //    List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);
    //    String similarContext =
    //            similarDocuments.stream()
    //                    .map(Document::getText)
    //                    .collect(Collectors.joining(System.lineSeparator()));
    String llmResponse =
        chatClient
            .prompt()
            //                    .system(
            //                            promptSystemSpec ->
            //
            // promptSystemSpec.text(hrPromptTemplateDocument).param("documents", similarContext))
            .advisors(
                advisorSpec ->
                    advisorSpec.param(
                        ChatMemory.CONVERSATION_ID,
                        username != null && !username.isEmpty() ? username : "default"))
            .user(message)
            .call()
            .content();

    return ResponseEntity.ok(llmResponse);
  }

  @GetMapping("/web-search/chat")
  public ResponseEntity<String> webSearchChat(
      @RequestParam("message") String message, @RequestHeader("username") String username) {
    String llmResponse =
        webSearchChatClient
            .prompt()
            .advisors(
                adv ->
                    adv.param(
                        ChatMemory.CONVERSATION_ID,
                        username != null && !username.isEmpty() ? username : "default"))
            .user(message)
            .call()
            .content();
    return ResponseEntity.ok(llmResponse);
  }
}
