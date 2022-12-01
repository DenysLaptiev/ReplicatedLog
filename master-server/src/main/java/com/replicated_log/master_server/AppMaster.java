package com.replicated_log.master_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class AppMaster {

    public static void main(String[] args) {
        SpringApplication.run(AppMaster.class, args);
    }
}