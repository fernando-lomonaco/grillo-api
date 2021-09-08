package br.com.grillo.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import br.com.grillo.config.ClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GardeClient {

  private static final int TIMEOUT = 30;

  private final ClientProperties clientProperties;

  public String connectClient(final String key) throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();

    HttpClient httpClient = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(TIMEOUT))
      .build();

    try {

      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(clientProperties.getUrl() + "/" + key))
        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
        .timeout(Duration.ofSeconds(TIMEOUT))
        .build();

      HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
      
      if(response.statusCode() != 200)
        return "S";
      
      JsonNode node = mapper.readTree(response.body());
      String value = Objects.requireNonNullElse(node.get("value").asText(), "S");

      return value;

    } catch (InterruptedException | IOException e) {     
      throw new InterruptedException();
    }
  }

}
