package echo.controller;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

  private static final int count = 100;

  private final Tracer tracer;

  @Autowired
  public EchoController(Tracer tracer) {
    this.tracer = tracer;
  }

  @RequestMapping("/echo")
  public String b(@RequestParam String name) {
    try (ActiveSpan parent = tracer.buildSpan("echo").startActive()) {
      for (int i = 0; i < count; i++) {
        try (ActiveSpan child = tracer.buildSpan("noop").asChildOf(parent).startActive()) {
          noop();
        }
      }
    }
    return name;
  }

  private void noop() {
  }
}
