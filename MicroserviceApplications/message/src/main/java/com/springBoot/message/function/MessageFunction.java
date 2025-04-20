package com.springBoot.message.function;

import com.springBoot.message.dto.AccountMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunction {
    public static final Logger logger = LoggerFactory.getLogger(MessageFunction.class);

    //This will consume a message from send-communication, pass it through email, then sms, then publish it to communication-sent.
    @Bean
    public Function<AccountMessageDto,AccountMessageDto> email(){
        return accountMessageDto -> {
            logger.info("sending email with details: {}",accountMessageDto.toString());
            return accountMessageDto;
        };
    }

    @Bean Function<AccountMessageDto,Long> sms(){
        return accountMessageDto -> {
            logger.info("sending sms with details: {}",accountMessageDto);
            return accountMessageDto.accountNumber();
        };
    }
}
