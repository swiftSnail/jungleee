package com.swiftsnail.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yaoxm on 2016/3/3.
 */

@Configuration
@ComponentScan("com.swiftsnail")
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
//        container.setPort(9090);
    }
}
