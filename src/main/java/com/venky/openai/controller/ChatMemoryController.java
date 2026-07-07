package com.venky.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {
  private final ChatClient chatClientWithMemory;

  public ChatMemoryController(@Qualifier("chatClientWithMemory") ChatClient chatClientWithMemory) {
    this.chatClientWithMemory = chatClientWithMemory;
  }

  @GetMapping("/chat-memory")
  public ResponseEntity<String> chatWithMemory(
      @RequestParam("message") String message, @RequestHeader("username") String username) {
    String response =
        chatClientWithMemory
            .prompt()
            .user(message)
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
            .call()
            .content();
    return ResponseEntity.ok(response);
  }
}
