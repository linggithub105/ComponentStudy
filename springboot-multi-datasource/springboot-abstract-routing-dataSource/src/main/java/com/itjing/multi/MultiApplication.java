package com.itjing.multi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author lijing
 * @date 2022年06月15日 20:24
 * @description
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiApplication.class, args);
    }
}
