package kr.co.mz.resttemplate;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
public class ResttemplateApplication {

  @Bean
  ApplicationRunner init() {
    return args -> {
        // 환율 정보 가져오는 오픈 API
        // https://open.er-api.com/v6/latest
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://open.er-api.com/"));

      // 메서드가 호출될 때 요청을 수행하는 프록시를 만들어야 합니다.
      RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
      HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

      ExchangeRateApi api = factory.createClient(ExchangeRateApi.class);
      Map<String, Map<String, BigDecimal>> res = api.getLatest();
      System.out.println("1USD = " + res.get("rates").get("KRW") + "WON");
    };
  }

  interface ExchangeRateApi {
    @GetExchange("/v6/latest")
    Map getLatest();
  }

  public static void main(String[] args) {
    SpringApplication.run(ResttemplateApplication.class, args);
  }
}
