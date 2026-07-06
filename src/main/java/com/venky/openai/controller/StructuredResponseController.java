package com.venky.openai.controller;

import com.venky.openai.model.CountryDetails;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StructuredResponseController {
  private final ChatClient chatClient;

  public StructuredResponseController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @GetMapping("/country-cities")
  public ResponseEntity<CountryDetails> chat(@RequestParam("message") String message) {
    //    CountryCities countryCities =
    //        chatClient.prompt().user(message).call().entity(CountryCities.class);
    CountryDetails countryDetails =
        chatClient
            .prompt()
            .user(message)
            .call()
            .entity(new BeanOutputConverter<>(CountryDetails.class));
    return ResponseEntity.ok(countryDetails);
  }

  @GetMapping("/city-list")
  public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
    List<String> countryCities =
        chatClient.prompt().user(message).call().entity(new ListOutputConverter());
    return ResponseEntity.ok(countryCities);
  }

  @GetMapping("/country-cities-map")
  public ResponseEntity<Map<String, Object>> chatMap(@RequestParam("message") String message) {
    Map<String, Object> countryCities =
        chatClient.prompt().user(message).call().entity(new MapOutputConverter());
    return ResponseEntity.ok(countryCities);
  }

  @GetMapping("/country-detail-list")
  public ResponseEntity<List<CountryDetails>> chatCountryDetailsList(
      @RequestParam("message") String message) {
    List<CountryDetails> countryCities =
        chatClient
            .prompt()
            .user(message)
            .call()
            .entity(new ParameterizedTypeReference<List<CountryDetails>>() {});
    return ResponseEntity.ok(countryCities);
  }
}
