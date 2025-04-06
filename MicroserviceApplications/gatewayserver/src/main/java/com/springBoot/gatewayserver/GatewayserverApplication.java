package com.springBoot.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

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
						)
						.uri("lb://LOANS")
				)
				.build();
	}
}
