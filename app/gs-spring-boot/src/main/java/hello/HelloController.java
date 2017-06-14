package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class HelloController {

  private final EchoService echoService;
  private final AsyncRestTemplate restTemplate;

  @Autowired
  public HelloController(EchoService echoService, AsyncRestTemplate restTemplate) {
    this.echoService = echoService;
    this.restTemplate = restTemplate;
  }

  @RequestMapping("/a")
  public String a() throws ExecutionException, InterruptedException {

    CompletableFuture<String> r1 = echoService.findUser("Imperial");
    CompletableFuture<String> r2 = echoService.findUser("College");
    CompletableFuture<String> r3 = echoService.findUser("London");

    CompletableFuture.allOf(r1, r2, r3).join();

    return String.format("My school is %s %s %s.", r1.get(), r2.get(), r3.get());
  }

  @RequestMapping("/c")
  public String t() throws ExecutionException, InterruptedException {
    ListenableFuture<ResponseEntity<String>> imperial = restTemplate.getForEntity("http://mockdb:8080/echo?name=Imperial", String.class);
    ListenableFuture<ResponseEntity<String>> college  = restTemplate.getForEntity("http://mockdb:8080/echo?name=College", String.class);
    ListenableFuture<ResponseEntity<String>> london = restTemplate.getForEntity("http://mockdb:8080/echo?name=London", String.class);
    return String.format("My school is %s %s %s.", imperial.get().getBody(), college.get().getBody(), london.get().getBody());
  }
}
