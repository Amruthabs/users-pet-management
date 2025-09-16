package com.example.app;

import org.springframework.boot.SpringApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 * Application bootstrap.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@Slf4j
public class DemoApplication {
    public static void main(String[] args) {
        log.info("Starting DemoApplication...");
        var context = SpringApplication.run(DemoApplication.class, args);
        log.info("DemoApplication started successfully with {} beans", context.getBeanDefinitionCount());
    }
}
