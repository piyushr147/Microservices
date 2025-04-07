package com.springBoot.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/banking/accounts/**")
                        .filters(filter ->
                                filter.rewritePath("banking/accounts/?(?<remaining>.*)","/${remaining}")
                                        .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
                                                .setFallbackUri("forward:/contactSupport")
                                        )
                        )
                        .uri("lb://ACCOUNTS")
                )
                .route(p -> p
                        .path("/banking/cards/**")
                        .filters(filter ->
                                filter.rewritePath("banking/cards/?(?<remaining>.*)","/${remaining}")
                                        .retry(retryConfig -> retryConfig
                                                .setMethods(HttpMethod.GET)
                                                .setRetries(3)
                                                .setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
                        )
                        .uri("lb://CARDS")
                )
                .route(p -> p
                        .path("/banking/loans/**")
                        .filters(filter ->
                                filter.rewritePath("banking/loans/?(?<remaining>.*)","/${remaining}")
                                        .requestRateLimiter(config -> config
                                                .setRateLimiter(createRedisRateLimiter())
                                                .setKeyResolver(createKeyResolver()))
                        )
                        .uri("lb://LOANS")
                )
                .build();
    }

    @Bean
    public KeyResolver createKeyResolver() {
        return (exchange -> Mono
                .justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous"));
//        return new KeyResolver() {
//            @Override
//            public Mono<String> resolve(ServerWebExchange exchange) {
//                return Mono.justOrEmpty(exchange.getRequest().getQueryParams().getFirst("user")).defaultIfEmpty("anonymous");
//            }
//        };
    }

    @Bean
    public RedisRateLimiter createRedisRateLimiter() {
        return new RedisRateLimiter(1,60,60);
    }
}
