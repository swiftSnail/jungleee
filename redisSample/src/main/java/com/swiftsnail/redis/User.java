package com.swiftsnail.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by yaoxm on 2016/4/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String fullName;
    private Date lastLogin;
}
