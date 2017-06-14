package hello;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.context.TracingUtils;
import io.opentracing.Tracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableAsync
public class Application extends AsyncConfigurerSupport {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public Executor getAsyncExecutor() {
    return TracingUtils.tracedExecutor(Executors.newFixedThreadPool(2));
  }

  @Bean
  public Tracer tracer() {
    return Configuration.fromEnv().getTracer();
  }

  @Bean
  public AsyncRestTemplate asyncRestTemplate() {
    return new AsyncRestTemplate();
  }
}
