package com.swiftsnail.redis;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by yaoxm on 2016/4/26.
 */
@Repository
public class UserRepository {
    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public void add(User user) {
        redisTemplate.opsForValue().set(user.getLogin(), user);
    }

    public User get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
