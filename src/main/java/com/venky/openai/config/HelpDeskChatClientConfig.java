package com.venky.openai.config;

import com.venky.openai.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class HelpDeskChatClientConfig {

  @Value("classpath:/promptTemplate/helpDeskSystemPromptTemplate.st")
  Resource systemPromptTemplate;

  @Bean("helpDeskChatClient")
  public ChatClient chatClient(
      ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, TimeTools timeTools) {
    Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
    return chatClientBuilder
        .defaultSystem(systemPromptTemplate)
        .defaultAdvisors(List.of(memoryAdvisor))
        .defaultTools(timeTools)
        .build();
  }

  @Bean
  ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
    return new DefaultToolExecutionExceptionProcessor(true);
  }
}
