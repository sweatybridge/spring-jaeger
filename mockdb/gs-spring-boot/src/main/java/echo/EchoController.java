package echo;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

  private static final int count = Integer.parseInt(System.getProperty("noop.count", "1"));

  private final Tracer tracer;

  @Autowired
  public EchoController(Tracer tracer) {
    this.tracer = tracer;
  }

  @RequestMapping("/echo")
  public String echo(@RequestParam String name) {
    ActiveSpan remote = tracer.activeSpan();
    try (ActiveSpan parent = tracer.buildSpan("echo").asChildOf(remote.context()).startActive()) {
      for (int i = 0; i < count; i++) {
        try (ActiveSpan child = tracer.buildSpan("noop").asChildOf(parent.context()).startActive()) {
          noop();
        }
      }
    }

    return name;
  }

  private void noop() {
  }
}
