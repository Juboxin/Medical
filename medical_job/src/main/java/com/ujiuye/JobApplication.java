package com.ujiuye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author JuBoxin
 * @date 2021/5/21 - 18:04
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//允许自动注入quartz
@EnableScheduling
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class);
    }
}
