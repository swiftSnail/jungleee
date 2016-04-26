package com.swiftsnail.springnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * Created by yaoxm on 2016/2/29.
 */
@Component
@Slf4j
public class HttpServer {

    @Autowired
    @Qualifier("serverBootstrap")
    @Getter
    @Setter
    private ServerBootstrap serverBootstrap;

    @Autowired
    @Qualifier("inetSocketAddress")
    @Getter
    @Setter
    private InetSocketAddress inetSocketAddress;

    private ChannelFuture serverChannelFuture;

    @PostConstruct
    public void start() throws InterruptedException {
        log.info("server starting at port: " + inetSocketAddress.getPort());
        serverChannelFuture = serverBootstrap.bind(inetSocketAddress).sync();
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        serverChannelFuture.channel().closeFuture().sync();
    }


}
