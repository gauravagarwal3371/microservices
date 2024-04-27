package com.codergv.feecollms.config;

import com.codergv.feecollms.client.ReceiptClient;
import com.codergv.feecollms.client.StudentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableRetry
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public WebClient receiptWebClient() {
        return WebClient.builder()
                .baseUrl("http://receipt-service")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public ReceiptClient receiptClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(receiptWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(ReceiptClient.class);
    }

    @Bean
    public WebClient studentWebClient() {
        return WebClient.builder()
                .baseUrl("http://student-service")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public StudentClient studentClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(studentWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(StudentClient.class);
    }
}
