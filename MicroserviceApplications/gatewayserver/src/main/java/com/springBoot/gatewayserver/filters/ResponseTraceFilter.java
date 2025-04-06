package com.springBoot.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    //This is the same as RequestFilter but as GlobalFilter is a FunctionalInterface(interface with only one function)
    //so instead of creating a class and implementing the class with GlobalFilter Interface we can directly write the
    //logic as a lambda expression and return it through @Bean method.
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            //This part is executed after the response is returned from the downstream service (post-processing).
            //.then(...) means:“After the request has finished being processed by the rest of the chain...”
            //Mono.fromRunnable(() -> { ... }): Creates a reactive task (no return value) that runs your custom logic after the response.
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = filterUtility.getCorrelationId(requestHeaders);
                if(exchange.getRequest().getHeaders().containsKey(FilterUtility.CORRELATION_ID)){
                    logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                    exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                }
            }));
        };
    }
}