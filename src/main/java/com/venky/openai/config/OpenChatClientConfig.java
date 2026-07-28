package com.venky.openai.config;

import java.util.List;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenChatClientConfig {

  @Bean("openChatClient")
  public ChatClient chatClient(
      ChatClient.Builder chatClientBuilder, SemanticCacheAdvisor semanticCacheAdvisor) {
    return chatClientBuilder.defaultAdvisors(List.of(semanticCacheAdvisor)).build();
  }
}
