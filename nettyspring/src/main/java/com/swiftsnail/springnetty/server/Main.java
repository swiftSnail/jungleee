package com.swiftsnail.springnetty.server;

import com.swiftsnail.springnetty.config.SpringConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by yaoxm on 2016/2/29.
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("starting appliction...");

        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.registerShutdownHook();
    }
}
