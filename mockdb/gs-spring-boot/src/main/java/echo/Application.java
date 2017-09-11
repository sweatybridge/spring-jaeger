package echo;

import com.uber.jaeger.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableAsync
public class Application extends AsyncConfigurerSupport {

  @Autowired
  private Tracer tracer;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public Executor getAsyncExecutor() {
    return new TracedExecutorService(Executors.newFixedThreadPool(2), tracer);
  }

  @Bean
  public Tracer tracer() {
    return Configuration.fromEnv().getTracer();
  }
}
