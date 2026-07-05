package com.venky.openai.config;

import com.venky.openai.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    var openAIChatOptions =
        OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8).maxCompletionTokens(100);

    return chatClientBuilder
        .defaultOptions(openAIChatOptions)
        //        .defaultAdvisors(new SimpleLoggerAdvisor())
        .defaultAdvisors(new TokenUsageAuditAdvisor())
        .defaultSystem(
            """
                                        You are an internal HR assistant. Your role is to help\s
                                        employees with questions related to HR policies, such as\s
                                        leave policies, working hours, benefits, and code of conduct.
                                        If a user asks for help with anything outside of these topics,\s
                                        kindly inform them that you can only assist with queries related to\s
                                        HR policies.
                                        """)
        //            .defaultUser("")
        .build();
  }
}
