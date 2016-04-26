package com.swiftsnail.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by yaoxm on 2016/3/3.
 */

@Service("emailService")
@Profile("product")
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Override
    public void send(String mailContent) {
        log.info("product send email!");
    }
}
