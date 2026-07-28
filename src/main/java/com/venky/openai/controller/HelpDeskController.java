package com.venky.openai.controller;

import com.venky.openai.tools.HelpDeskTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/tools")
public class HelpDeskController {
  private final ChatClient chatClient;
  private final HelpDeskTools helpDeskTools;

  public HelpDeskController(@Qualifier("helpDeskChatClient") ChatClient chatClient, HelpDeskTools helpDeskTools) {
    this.chatClient = chatClient;
    this.helpDeskTools = helpDeskTools;
  }

  @GetMapping("/help-desk")
  public ResponseEntity<String> helpDesk(
      @RequestHeader("username") String username, @RequestParam("message") String message) {
    String answer =
        chatClient
            .prompt()
            .advisors(a -> a.param(CONVERSATION_ID, username))
            .tools(helpDeskTools)
                .toolContext(Map.of("username", username))
            .user(message)
            .call()
            .content();
    return ResponseEntity.ok(answer);
  }
}
