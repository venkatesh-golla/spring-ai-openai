package com.venky.openai.config;

import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.RedisClient;

@Configuration
public class SemanticCacheConfig {
  @Bean
  RedisClient redisClient(
      @Value("${spring.data.redis.host:localhost}") String host,
      @Value("${spring.data.redis.port:6379}") int port) {
    return new RedisClient.Builder().hostAndPort(host, port).build();
  }

  @Bean
  public SemanticCache semanticCache(RedisClient redisClient, EmbeddingModel embeddingModel) {
    return DefaultSemanticCache.builder()
        .jedisClient(redisClient)
        .embeddingModel(embeddingModel)
        .similarityThreshold(0.9)
        .indexName("semantic_cache_index")
        .prefix("cache:")
        .build();
  }

  @Bean
  public SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache) {
    return SemanticCacheAdvisor.builder().cache(semanticCache).build();
  }
}
