package com.venky.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {
  private String promptTemplate =
      """
            A customer named {customerName} sent the following message:
                           "{customerMessage}"

            Write a polite and helpful email response addressing the issue.
            Maintain a professional tone and provide reassurance.

            Respond as if you're writing the email body only. Don't include subject,
            signature
            """;
  private final ChatClient chatClient;

  public PromptTemplateController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/email")
  public String email(
      @RequestParam("customerName") String customerName,
      @RequestParam("customerMessage") String customerMessage) {
    return chatClient
        .prompt()
        .system(
            """
                        You are a professional customer service assistant which helps drafting email
                        responses to improve the productivity of the customer support team
                        """)
        .user(
            (promptTemplateSpec) ->
                promptTemplateSpec
                    .text(promptTemplate)
                    .param("customerName", customerName)
                    .param("customerMessage", customerMessage))
        .call()
        .content();
  }
}
