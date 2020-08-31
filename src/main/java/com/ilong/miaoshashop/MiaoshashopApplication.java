package com.ilong.miaoshashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MiaoshashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshashopApplication.class, args);
    }

}
