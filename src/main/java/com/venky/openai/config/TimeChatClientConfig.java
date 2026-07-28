package com.venky.openai.config;

import com.venky.openai.tools.TimeTools;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeChatClientConfig {

  @Bean("timeChatClient")
  public ChatClient chatClient(
      ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, TimeTools timeTools) {
    Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
    return chatClientBuilder
        .defaultTools(timeTools)
        .defaultAdvisors(List.of(memoryAdvisor))
        .build();
  }
}
