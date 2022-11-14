package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "org.example.mapper")
@SpringBootApplication
public class FrontUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontUserApplication.class, args);
    }
}
