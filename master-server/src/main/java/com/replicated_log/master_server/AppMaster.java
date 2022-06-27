package com.replicated_log.master_server;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AppMaster {

    public static void main(String[] args) {
        SpringApplication.run(AppMaster.class, args);
    }
}