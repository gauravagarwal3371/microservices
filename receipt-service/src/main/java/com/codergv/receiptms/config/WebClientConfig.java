package com.codergv.receiptms.config;

import com.codergv.receiptms.client.FeeCollectionClient;
import com.codergv.receiptms.client.StudentClient;
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

    @Bean
    public WebClient feeCollectionWebClient() {
        return WebClient.builder()
                .baseUrl("http://fee-coll-service")
                .filter(filterFunction)
                .build();
    }
    @Bean
    public FeeCollectionClient feeCollectionClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(feeCollectionWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(FeeCollectionClient.class);
    }
}
