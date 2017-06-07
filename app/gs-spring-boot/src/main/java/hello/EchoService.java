package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class EchoService {

  private final RestTemplate restTemplate;

  @Autowired
  public EchoService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  public CompletableFuture<String> findUser(String user) {
    String results = restTemplate.getForObject("http://mockdb:8080/echo?name=" + user, String.class);
    return CompletableFuture.completedFuture(results);
  }

}
