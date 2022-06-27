package com.replicated_log.secondary_server_1;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AppSecondary1 {

    public static void main(String[] args) {
        SpringApplication.run(AppSecondary1.class, args);
    }
}

