package com.swiftsnail.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yaoxm on 2016/4/2 0002.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMsg {
    private int code;
    private String msg;
}
