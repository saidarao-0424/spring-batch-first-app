package com.saida.spring.batch.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(value="com.saida.spring.batch")
@PropertySource("classpath:application.properties")
@EntityScan( basePackages = {"com.saida.spring.batch.domain"} )
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}