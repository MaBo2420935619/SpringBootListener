package com.mabo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class SpringBootListener {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootListener.class, args);
    }

}
