package com.springBoot.gatewayserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        return serverHttpSecurity.authorizeExchange(exchange -> exchange
                .pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/banking/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("/banking/cards/**").hasRole("CARDS")
                        .pathMatchers("/banking/loans/**").hasRole("LOANS")
                )
                //This app should validate JWT tokens using default logic, and you’ll find the required settings in my application.yml
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(csrfSpec -> csrfSpec.disable())
                .build();
    }

    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
        //In reactive applications (WebFlux), Spring Security expects a Converter<Jwt, Mono<? extends AbstractAuthenticationToken>>.
        //Since JwtAuthenticationConverter is blocking, the adapter bridges the gap.
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
