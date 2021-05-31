package com.ujiuye;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JuBoxin
 * @date 2021/5/18 - 13:34
 */
@SpringBootApplication
@MapperScan("com.ujiuye.mapper")
public class MedicalProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalProviderApplication.class);
    }
}
