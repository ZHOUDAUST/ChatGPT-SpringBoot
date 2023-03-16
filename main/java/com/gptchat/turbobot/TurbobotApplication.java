package com.gptchat.turbobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"classpath*:application-*.xml"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TurbobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbobotApplication.class, args);
    }

}
