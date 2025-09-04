package com.example.erm_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.erm_demo.adapter.out.feign.client")
public class ErmDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErmDemoApplication.class, args);
    }

}
