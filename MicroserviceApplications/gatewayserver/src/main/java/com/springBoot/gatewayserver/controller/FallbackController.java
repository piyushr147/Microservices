package com.springBoot.gatewayserver.controller;

import org.springframework.context.annotation.Fallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/contactSupport")
    public Mono<String> fallbackResponse(){
        return Mono.just("Error in api response kindly reach out the support team send a sms to 100 or mail to abc@gmail.com");
    }
}
