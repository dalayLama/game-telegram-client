package org.quizstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAspectJAutoProxy()
public class QuizStorageTelegramClient {

    public static void main(String[] args) {
        SpringApplication.run(QuizStorageTelegramClient.class, args);
    }

}
