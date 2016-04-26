package com.swiftsnail.app;

import com.swiftsnail.redis.User;
import com.swiftsnail.redis.UserRepository;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Date;

/**
 * Created by yaoxm on 2016/4/26.
 */
public class Main {
    public static void main(String[] args) throws RunnerException {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.registerShutdownHook();

        UserRepository userRepository = (UserRepository) ctx.getBean("userRepository");
//        userRepository.add(new User("yaoxm","yaoxiaoming",new Date()));
        User user = getUser(userRepository);
        System.out.println(user);

        Options opt = (new OptionsBuilder()).include(Main.class.getSimpleName()).forks(4).build();
        (new Runner(opt)).run();
    }

    public static User getUser(UserRepository userRepository) {
        return userRepository.get("yaoxm");
    }

}
