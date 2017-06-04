package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// notice there is no tracing code in this class
@RestController
public class HelloController {

  private final EchoService echoService;

  @Autowired
  public HelloController(EchoService echoService) {
    this.echoService = echoService;
  }

  @RequestMapping("/a")
  public String a() throws ExecutionException, InterruptedException {

    CompletableFuture<String> r1 = echoService.findUser("Imperial");
    CompletableFuture<String> r2 = echoService.findUser("College");
    CompletableFuture<String> r3 = echoService.findUser("London");

    CompletableFuture.allOf(r1, r2, r3).join();

    return String.format("My school is %s %s %s.", r1.get(), r2.get(), r3.get());
  }
}
