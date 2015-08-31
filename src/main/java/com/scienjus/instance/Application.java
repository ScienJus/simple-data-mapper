package com.scienjus.instance;

import com.scienjus.base.util.DataMapperInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author XieEnlong
 * @date 2015/8/31.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scienjus")
@EnableConfigurationProperties(DataMapperInitializer.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
