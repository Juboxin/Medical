package com.ujiuye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author JuBoxin
 * @date 2021/5/18 - 16:58
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MedicalControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalControllerApplication.class);
    }
}
