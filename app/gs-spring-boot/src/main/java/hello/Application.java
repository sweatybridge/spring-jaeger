package hello;

import com.uber.jaeger.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

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

  @Bean("executor")
  @Override
  public Executor getAsyncExecutor() {
    return new TracedExecutorService(Executors.newFixedThreadPool(2), tracer);
  }

  @Bean
  public Tracer tracer() {
    return Configuration.fromEnv().getTracer();
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Bean
  public AsyncRestTemplate asyncRestTemplate() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(2);
    taskExecutor.setMaxPoolSize(2);
    taskExecutor.setQueueCapacity(500);
    taskExecutor.initialize();
    return new AsyncRestTemplate(taskExecutor);
  }
}
