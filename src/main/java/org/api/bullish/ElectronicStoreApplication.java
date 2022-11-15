package org.api.bullish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebMvc
@EnableSwagger2
public class ElectronicStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }
}