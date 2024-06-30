package com.seekerhut;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.boot.SpringApplication.*;

@EnableSwagger2
@SpringBootApplication
public class GirlfriendApplication {

    public static void main(String[] args) {
        run(GirlfriendApplication.class, args);
    }
}