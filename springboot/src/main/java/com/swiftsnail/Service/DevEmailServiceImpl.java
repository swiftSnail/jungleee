package com.swiftsnail.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by yaoxm on 2016/3/3.
 */
@Service("devEmailService")
@Profile("dev")
@Slf4j
public class DevEmailServiceImpl implements EmailService {
    @Override
    public void send(String mailContent) {
        log.info("dev environment send email!");
    }
}
