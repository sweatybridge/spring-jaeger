package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@RestController
public class HelloController {

  private final AsyncRestTemplate asyncRestTemplate;
  private final RestTemplate restTemplate;
  private final Executor executor;

  @Autowired
  public HelloController(
      AsyncRestTemplate asyncRestTemplate,
      RestTemplate restTemplate,
      Executor executor) {
    this.asyncRestTemplate = asyncRestTemplate;
    this.restTemplate = restTemplate;
    this.executor = executor;
  }

  @RequestMapping("/hello")
  public String hello() throws ExecutionException, InterruptedException {
    ListenableFuture<ResponseEntity<String>> imperial = asyncRestTemplate.getForEntity(
        "http://mockdb:8080/echo?name=Imperial", String.class);
    ListenableFuture<ResponseEntity<String>> college = asyncRestTemplate.getForEntity(
        "http://mockdb:8080/echo?name=College", String.class);
    ListenableFuture<ResponseEntity<String>> london = asyncRestTemplate.getForEntity(
        "http://mockdb:8080/echo?name=London", String.class);
    return String.format("My school is %s %s %s.",
        imperial.get().getBody(), college.get().getBody(), london.get().getBody());
  }

  @RequestMapping("/search")
  public String search(@RequestParam Integer echo) throws ExecutionException, InterruptedException {
    CompletableFuture<String>[] rs = new CompletableFuture[echo];
    for (int i = 0; i < echo; i++) {
      String url = String.format("http://mockdb:8080/echo?name=%d", i);
      rs[i] = CompletableFuture.supplyAsync(() -> 
          restTemplate.getForObject(url, String.class), executor);
    }

    CompletableFuture.allOf(rs).join();
    return "done";
  }
}
